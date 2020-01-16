--Oppgave 2
--a)
select *
from timelistelinje
where timelistenr = 3;

--b)
select count(distinct timelistenr) as antalltimelister
from timelistelinje;

--c)
select count(*)
from timeliste
where utbetalt is null;

--d)
select distinct status, count(status) as antalltimelister
from timeliste
group by status;

--e)
select count(linjenr) as antall, count(pause) as antallmedpause
from timelistelinje;

--f)
select *
from timelistelinje
where pause is null;

--Oppgave 3
--a)
select sum(varighet)/60.0 as timerikkeutbetalt
from timeliste inner join varighet on timeliste.timelistenr = varighet.timelistenr
where status !~~ 'utbetalt';

--b)
select distinct on (timeliste.timelistenr) timeliste.timelistenr, timeliste.beskrivelse
from timeliste inner join timelistelinje
on timeliste.timelistenr = timelistelinje.timelistenr
where timelistelinje.beskrivelse like '%Test%' or timelistelinje.beskrivelse like '%test%' order by timeliste.timelistenr;

--c)
select varighet.timelistenr, varighet.linjenr, varighet.varighet, timelistelinje.beskrivelse
from varighet natural join timelistelinje order by varighet.varighet DESC LIMIT 5;

--d)
select  timeliste.timelistenr, count(timelistelinje.timelistenr) as antall
from timeliste left join timelistelinje
on timeliste.timelistenr = timelistelinje.timelistenr group by timeliste.timelistenr order by timeliste.timelistenr;

--e)
select (sum(varighet)/60.0)*200 as pengerutbetalt
from timeliste inner join varighet on timeliste.timelistenr = varighet.timelistenr
where status ~~ 'utbetalt';

--f)
select timeliste.timelistenr, count(timelistelinje.timelistenr) as antallutenpause
from timelistelinje left join timeliste on timelistelinje.timelistenr = timeliste.timelistenr
where pause is null group by timeliste.timelistenr
order by timeliste.timelistenr;
