/* Etat vaccin non utilisé*/
Create view etat_vaccin_disponible as
select
    count(id) as quantite,
    centre_id,
    vaccin_id
from
    stock
where
    est_utilise is false
group by
    vaccin_id,
    centre_id;




/*Fin etat vaccin non utilise*/
/*Etat vaccin par date peremption*/
Create view etat_vaccin_par_peremption as
select
    count(id) as quantite,
    date_peremption,
    centre_id,
    vaccin_id
from
    stock
where
    est_utilise is true
group by
    centre_id,
    vaccin_id,
    date_peremption;




/*Fin etat vaccin par date peremption*/
/*Stat moyenne par jour*/
Create view demande_traite_par_jour as
Select
    count(d.id) as sub_total,
    vaccin_id,
    centre_id,
    date_demande
from
    demande_vaccin d
where
    id in (
        select
            demande_vaccin_id
        from
            historique
    )
group by
    vaccin_id,
    date_demande,
    centre_id;




Create view demande_date_max_min as
Select
    max(date_demande) as max_date,
    min(date_demande) as min_date,
    vaccin_id,
    centre_id,
    sum(sub_total) as total
from
    demande_traite_par_jour
group by
    vaccin_id,
    centre_id;




Create view demande_moyenne_par_jour as
select
    sum(total / abs(min_date :: date - max_date :: date)) as moyenne,
    vaccin_id,
    centre_id
from
    demande_date_max_min
group by
    vaccin_id,
    centre_id;




/*Fin stat moyenne par jour*/