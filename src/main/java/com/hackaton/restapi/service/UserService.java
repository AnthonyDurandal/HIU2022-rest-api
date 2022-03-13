package com.hackaton.restapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Role;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.entity.UserToken;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.RoleRepository;
import com.hackaton.restapi.repository.UserRepository;
import com.hackaton.restapi.repository.UserTokenRepository;
import com.hackaton.restapi.util.Util;

import com.hackaton.restapi.query.SearchCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
            UserTokenRepository userTokenRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.roleRepository = roleRepository;
    }

    public User addNewUser(User user, String role) {
        if (user == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if (user.getUsername() == null ||
                user.getPassword() == null) {
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        }
        String regexMail = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!user.getUsername().matches(regexMail)) {
            throw new ApiRequestException("Format de mail invalide");
        }
        if (verifAccent(user.getPassword())) {
            throw new ApiRequestException("Le mot de passe ne doit pas contenir d'accent");
        }
        if (user.getPassword().length() < 8) {
            throw new ApiRequestException("Le mot de passe doit contenir au moins 8 caractères");
        }
        Optional<Role> roleToAdd = roleRepository.findByNom(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getDateCreation() == null) {
            user.setDateCreation(new Timestamp(System.currentTimeMillis()));
        }
        user.setRole(roleToAdd.get());
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new ApiRequestException("Username non disponible");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, User user) {
        User userModif = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException("Cet user n'existe pas"));

        if (user.getUsername() != null) {
            if (user.getUsername().length() == 0) {
                throw new ApiRequestException("L'username ne peut pas être vide");
            }
            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
            if (userOptional.isPresent() && userOptional.get().getId() != userId) {
                throw new ApiRequestException("Username déja utilisé");
            }
            userModif.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            if (user.getPassword().length() == 0) {
                throw new ApiRequestException("Le mot de passe ne peut pas être vide");
            }
            String newPassword = passwordEncoder.encode(user.getPassword());
            userModif.setPassword(newPassword);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        Optional<User> res = userRepository.findByUsername(username);
        if (!res.isPresent()) {
            throw new ApiRequestException("Cet user n'existe pas");
        }
        return res;
    }

    public void deconnexion(User user) {
        User userLogout = userRepository.findById(user.getId())
                .orElseThrow(() -> new ApiRequestException("Cet user n'existe pas"));
        Pageable pageable = Util.pageable("datePeremption.desc", 1, 1);
        Page<UserToken> userTokenPage = userTokenRepository.findByUserId(userLogout.getId(), pageable);
        UserToken userToken = (UserToken) userTokenPage.getContent().get(0);
        userTokenRepository.delete(userToken);
    }

    public boolean verifAccent(String mot) {
        String accent = "à;â;ç;é;è;ê;ë;î;ï;ô;û;ù;ü;ÿ;ñ;æ;œ";
        String[] lAccents = accent.split(";");
        for (String string : lAccents) {
            if (mot.contains(string)) {
                return true;
            }
        }
        return false;
    }

    public Page<User> getUser(String sort, Integer page, Integer size,
            String id, String idRole) {
        Specification<User> specification = getAllSpecifications(id, idRole);
        specification = (specification != null) ? Specification.where(specification) : null;
        if (page == null)
            page = 1;
        if (size == null || size == 0)
            size = userRepository.findAll().size();
        if (page < 1)
            throw new ApiRequestException("L'index de la page ne doit pas être inférieur à un : page = " + page);
        if (size < 0)
            throw new ApiRequestException(
                    "La taille de la page ne doit pas être inférieure à un : size = " + size + "    " + (size == 0));
        Pageable pageable = Util.pageable(sort, page - 1, size);
        Page<User> res = userRepository.findAll(specification, pageable);
        if (res.getSize() == 0)
            throw new ApiRequestException("Aucun élement trouvé");
        return res;
    }

    public Specification<User> getAllSpecifications(String id, String idRole) {
        Specification<User> specification = ajouterSiNonNull(null, equalsId(id));
        specification = ajouterSiNonNull(specification, equalsIdRole(idRole));
        return specification;
    }

    public static Specification<User> equalsId(String id) {
        if (Util.isNullOrEmpty(id))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria("id", id);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(critere.getKey()),
                        critere.getValue());
            default:
                return null;
            // throw new Exception("TypeSignalementService.equalsId() n'accepte que le
            // critère '='
            // ");
        }
    }

    public static Specification<User> equalsIdRole(String id) {
        if (Util.isNullOrEmpty(id))
            return null;
        SearchCriteria critere = SearchCriteria.getSearchCriteria("id", id);
        switch (critere.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(critere.getKey()),
                        critere.getValue());
            default:
                return null;
            // throw new Exception("TypeSignalementService.equalsId() n'accepte que le
            // critère '='
            // ");
        }
    }

    public Specification<User> ajouterSiNonNull(Specification<User> specificationMere,
            Specification<User> specificationAAjouter) {
        if (specificationAAjouter == null)
            return specificationMere;
        // choix 1: ajout de where :if(specificationMere == null)return
        // Specification.where(specificationAAjouter);
        /* choix 2: on ajoute pas directement where */
        if (specificationMere == null)
            return specificationAAjouter;
        return specificationMere.and(specificationAAjouter);
    }
}
