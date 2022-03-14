// package com.hackaton.restapi.view;

// import javax.persistence.Entity;
// import javax.persistence.Id;

// import com.hackaton.restapi.entity.Centre;
// import com.hackaton.restapi.entity.Vaccin;

// import org.hibernate.annotations.Immutable;
// import org.hibernate.annotations.Subselect;

// @Entity
// @Immutable
// @Subselect("select count(id) as quantite, centre_id, vaccin_id from stock where est_utilise is false group by vaccin_id, centre_id")
// public class EtatVaccinDisponible {
//     Integer quantite;
//     @Id
//     Long centre_id;
//     @Id
//     Long vaccin_id;
// }
