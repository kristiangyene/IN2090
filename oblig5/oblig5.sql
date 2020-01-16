--Oppgave 1:
select filmcharacter as rollefigurnavn, count(*) as antall from filmcharacter
group by filmcharacter having count(*) > 2000 order by count(*) desc;

--Oppgave 2:
--2a: INNER JOIN

select film.title, film.prodyear from film
inner join filmparticipation
on film.filmid = filmparticipation.filmid
inner join person
on filmparticipation.personid = person.personid
where filmparticipation.parttype ~~ 'director'
and person.firstname ~~ 'Stanley' and person.lastname ~~ 'Kubrick';

--2b: NATURAL JOIN
select film.title, film.prodyear from film
natural join filmparticipation
natural join person
where filmparticipation.parttype ~~ 'director'
and person.firstname ~~ 'Stanley' and person.lastname ~~ 'Kubrick';

--2c: Implisitt join
select * from film f, filmparticipation fp, person p
where f.filmid = fp.filmid and fp.personid = p.personid and fp.parttype ~~ 'director'
and p.firstname ~~ 'Stanley' and p.lastname ~~ 'Kubrick';

--Oppgave 3:
select person.personid, person.firstname || ' ' || person.lastname
as name, title, filmcountry.country
from filmcharacter
natural join filmparticipation
natural join person
natural join film
natural join filmcountry

where person.firstname ~~'Ingrid' and filmcharacter.filmcharacter ~~'Ingrid'
and filmcharacter is not null;

--Oppgave 4:
select film.filmid, film.title, count(filmgenre.genre) as antall
from film left join filmgenre on film.filmid = filmgenre.filmid
where film.title like '%Antoine %'
group by film.filmid, film.title;

--Oppgave 5:
select film.title, filmparticipation.parttype as deltagelsestype, count(filmparticipation.parttype) as antall_deltagere
from film
natural join filmitem
natural join filmparticipation
where filmitem.filmtype like 'C'
and film.title like '%Lord of the Rings%'
group by film.title, film.prodyear, filmparticipation.parttype;

--Oppgave 6:
select title, prodyear from film where prodyear = (select min(prodyear) from film) group by title, prodyear;

--Oppgave 7:
select distinct film.title, film.prodyear from film,
filmgenre as genre1, filmgenre as genre2
where film.filmid = genre1.filmid and genre1.filmid = genre2.filmid
and genre1.genre = 'Film-Noir' and genre2.genre = 'Comedy';

--Oppgave 8:
(select title, prodyear from film where prodyear = (select min(prodyear) from film) group by title, prodyear)
union all
(select distinct film.title, film.prodyear from film,
filmgenre as genre1, filmgenre as genre2
where film.filmid = genre1.filmid and genre1.filmid = genre2.filmid
and genre1.genre = 'Film-Noir' and genre2.genre = 'Comedy');

--Oppgave 9
(select film.title, film.prodyear from film
inner join filmparticipation
on film.filmid = filmparticipation.filmid
inner join person
on filmparticipation.personid = person.personid
where filmparticipation.parttype ~~ 'director'
and person.firstname ~~ 'Stanley' and person.lastname ~~ 'Kubrick')
intersect all
(select film.title, film.prodyear from film
inner join filmparticipation
on film.filmid = filmparticipation.filmid
inner join person
on filmparticipation.personid = person.personid
where filmparticipation.parttype ~~ 'cast'
and person.firstname ~~ 'Stanley' and person.lastname ~~ 'Kubrick');

--Oppgave 10
select series.maintitle, filmrating.rank from series
inner join filmrating on series.seriesid = filmrating.filmid
where filmrating.votes > 1000 and filmrating.rank = (select max(filmrating.rank) from filmrating where filmrating.votes > 1000) group by series.maintitle, filmrating.rank;

--Oppgave 11
select country from filmcountry group by country having count(country) = 1;

--Oppgave 12
with unikcharacter as(select ch.partid, ur.filmcharacter
from(select filmcharacter, count(*) from filmcharacter
group by filmcharacter having count(*) = 1) as ur, filmcharacter as ch
where ur.filmcharacter = ch.filmcharacter)
select firstname || ' ' || lastname as name, count(*) as antall from person
natural join unikcharacter
natural join filmparticipation
group by name having count(*) > 199 order by antall desc;

--Oppgave 13
(select distinct firstname || ' ' || lastname as name from person
inner join filmparticipation on filmparticipation.personid = person.personid
inner join filmrating on filmrating.filmid = filmparticipation.filmid
where filmrating.votes > 60000 and filmparticipation.parttype ~~ 'director'
group by name, filmrating.rank having filmrating.rank >= 8)

except all

(select firstname || ' ' || lastname as name from person
inner join filmparticipation on filmparticipation.personid = person.personid
inner join filmrating on filmrating.filmid = filmparticipation.filmid
where filmrating.votes > 60000 and filmparticipation.parttype ~~ 'director'
group by name, filmrating.rank having filmrating.rank < 8);
