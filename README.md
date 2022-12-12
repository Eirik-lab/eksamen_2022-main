Oppgave 1
-
 - Det er flere grove feil som har blitt gjort ved dagen systemutviklingsprosess. Hovedsakelig er det tre devopsprinsipper man skal forholde seg til, med diverse underkategorier:
   - The principle of flow
     - Det at det bare blir deployet kode hver mandag i kvartalet er en stor strek i regninga når det kommer til flyt. \
        Det kan føre til at funksjonalitet som er ansett som uviktig, men nesten ferdig, kan bli underprioritert og satt på vent 
        slik at man kan rekke deployment-fristen på funksjonalitet som er ansett som viktigere. \
        Med andre ord kan det føre til at funksjonalitet som nesten er ferdig blir ikke blir deployet før mandag neste kvartal. 
        Mens utviklere tar ulønnsomme snarveier med funksjonalitet som er ansett som viktig og dermed bygger på seg en stor menge 
       "technical debt" i et forsøk på å rekke å deploye før fristen.
     - Automasjon
       - Selskapet deployet oftere før, men så ansatte flere testere og startet en prosess der utviklingsledere måtte se over og godkjenne alle leveranser. 
         Førte til ustabilitet, de senket derfor frekvensen på leveransene for å øke stabiliteten
         - Dette er en svært dårlig måte å løse et slikt problem på mtp. kontinuelig flyt. \
           En mye bedre løsning ville vært å bruke workflows/actions sammen med github rules for å automatisere testene og ikke la noen deployments gå videre til
           main branchen uten at elle actions ble godkjent. 
     - Collaborations
       - Hvert medlem av et devops team er ansvarlig for hver del av det operationalet av prosjektet, tross alt er hovedhensikten med devops at utviklerne og operatørene er de samme menneskene.
         Det betyr at et team er mest effektivt når der er relativt lite. Med andre ord så vil det å hyre inn en haug med andre folk for å se over og hjelpe til med det operationalet aspektet ved devops være counter-productive, 
         og motsier hele poenget med devops. Jo flere kokker desto mere søl.
     - Leveransen skjer ved at Utviklingsteamet bruker FTP til å overføre en Spring boot JAR sammen med dokumentasjon i en ZIP. En egen avdeling tar i mot disse filene og installerer i AWS / Produksjon.
       - Dette bryter med Automation, Continuous improvement og Collaboration prinsippene.
       - Hele teamet må være på samme platform for at det skal dette skal fungere ordentlig.
       - Ikke sikkert at maskinvaren som brukes i utvikling er samme som i  produksjon
   - The Principle of Feedback
     - Automasjon
       - Selskapet deployet oftere før, men så ansatte flere testere og startet en prosess der utviklingsledere måtte se over og godkjenne alle leveranser.
          Førte til ustabilitet, de senket derfor frekvensen på leveransene for å øke stabiliteten
         - Det å ansette flere testere, samt kreve at ledere skal ta seg tid til å se gjennom hver commit er urealistisk, tidkrevende og dyrt.\
           En mye bedre måte å løse et slikt problem på ville vært å la utviklerne som allerede jobbet med og hadde erfaring med shopifly se gjennom hverandes kode før deployment. +
           Dette kan lett løses ved at man setter på en "branch rule" som sier at man ikke kan deploye til main branchen før alle actions kjører grønnt og man har fått godkjenning av
           minst én annen utvikler. \
           Det vil si at om noen av actionsene ender opp med å kjøre rødt fikser man først det før man spør en annen utvikler om å ta seg tid til å se gjennom "merge-requesten". 
       - Om man ikke tester koden man har skrevet vil feil mest sannsynlig oppstå, en måte å løse dette på er å ikke la noen pulle til main uten at hvertfall en annen person ser gjennom koden, 
         den personen kan ta gå inn og teste alle enhetstestene og integrasjonstestene og se om de kjører,
         samt. se om tilstrekkelig antall tester er skrevet eller om det er funksjonalitet som er lagt inn i koden som blir pushet,
         men ikke dekket av test coverage. 
       - Man må også huske på og gi kontinuerlige tilbakemeldinger, også på prestasjon samt. funksjonalitet.
         Dvs. at om man ser at problemer ofte blir løst på en måte som skaper nye problemer må dette tas opp i plenum
         (eller med personen det gjelder) og så må man idemyldre etter måter å løse dette på i framtiden,
         slik at det samme problemet ikke gjentar seg om og om igjen.
   - The Principle of Continual learning and experimentation
     - Når de deployer, feiler det fortsatt ofte. Da ruller de tilbake til forrige versjon, og ny funksjonalitet blir derfor ofte forsinket i månedsvis.
       - Ifølge DevOps prinsippene burde utviklingsprosessen og vedlikeholdsprosessen se slik ut; Planlegg -> Bygg -> Test -> Deploy -> Operer -> Observer -> Gi kontinuerlig tilbakemeldinger -> Finn feil -> gjenta. \
         Om man ofte må rollbacke fordi koden feiler under deployment er nok ikke det fordi det er noe galt med koden. \
         Det skyldes nok mye heller fordi det er noe galt med måten systemet er satt opp. Da kan man jo alltids anta at problemer oppstår fordi et eller flere av disse stegene er borte,
         f.eks. om man ikke har satt opp noen metrics til å analysere data vil man da ikke kunne oppdage og løse feil som ellers ville vært åpenbare. 

Oppgave 2
-
###Hva kan sensor gjøre for at ingen kan pushe direkte til main branch?
- --
- Først går du inn i settings/branches i github repositoriet
- Velg branchen du vil legge til en regel for
- Deretter legger du til en branch protection rule og velger 'Lock branch' (Husk å gi regelen et navnet til branchen)
    * Dette gjør at branchen bare kan leses, og at ingen kan pushe til den
- Deretter velger du 'Do not allow bypassing the above settings'
    * Det vil si at regelene du har valgt gjelder de med administratorrettigheter også
- Trykk 'Create'/ 'Save Changes'

###Hva kan sensor gjøre for at kode kan pulles til main/ master branch på betingelsen at minst én må godkjenne den først?
- --
- Naviger til Branches og trykk på 'edit rule' om regelen som allerede er laget tilhører main branchen, hvis ikke så lager du en ny regel
- Fjern 'Lock branch' om det er nødvendig
- Velg 'Require a pull request before merging', så 'Require approvals', deretter setter du antall approvals godkjenninger før sammenslåing til 1

###Hva kan sensor gjøre for at kode merges til main branch kun når GitHub actions sin verifisering godkenner det?
- --
- Naviger til Branch rules
- Velg 'Require status checks to pass before merging' (og helst også 'Require branches to be up to date before merging')

Oppgave 3
-
##1

###Beskriv hva du må gjøre for å få workflow til å fungere med din DockerHub konto? Hvorfor feiler workflowen?
- --
- For å få workflowen til å fungere med min DockerHub konto må jeg
    * gå inn i Setting(repo ikke account)/Secrets/Actions
    * deretter lage to nye repository secrets kalt DOCKER_HUB_TOKEN og DOCKER_HUB_USERNAME, deretter legger jeg dockerhub brukernavnet under username og passordet under token

- Årsaken til at det ikke virket var at i workflowen under Login to Docker Hub hadde jeg oppgitt secrets som brukernavn og passord, men jeg hadde aldri oppgitt/ lagd noen secrets. Derfor da github actions prøvde å hente brukernavn og passord for å logge inn, feilet det fordi brukernavnet og passordet aldri var oppgitt.
    
##3

###Beskriv deretter med egne ord hva sensor må gjøre for å få sin fork til å laste opp container image til sitt eget ECR repo.
- --
- Naviger til .github/workflow/docker.yml
- Gå til actionen kalt Build and push Docker image to ecr
- Bytt ut alle instansene med 244530008913.dkr.ecr.eu-west-1.amazonaws.com/1045 til 244530008913.dkr.ecr.eu-west-1.amazonaws.com/<repo-et til sensor>

Oppgave 5
- 
###Forklar med egne ord. Hva er årsaken til dette problemet? Hvorfor forsøker Terraform å opprette en bucket, når den allerede eksisterer?
- --
- Årsaken til at github terraform forsøker å opprette en ny bucket når den forrige allerede eksisterer
  er at vi ikke har laget en backend og definert navnet på bucketen, key-et eller regionen.
  Vi kan løse dette ved å lette til;\
  backend "s3" { \
  bucket = <bucket.name> \
  key    = <key.name> \
  region = <region.name> \
  } \
  i provider.tf filen.

  I tillegg burde vi også legge til aws s3api get-bucket-location --bucket <bucket-name>> || aws s3 mb s3://<bucket-name>> i run konfigurasjonen for å forsikre oss om at når workflowen kjører så vil den opprette ny bucket om den allerede eksisterer.


