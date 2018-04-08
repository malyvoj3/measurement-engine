
Popis CD bakalarske prace

Nazev: Nastroj pro mereni datove kvality pomoci SQL dotazu
Autor: Vojtech Maly
Vedouci prace: Ing. Tereza Mlynarova, Ph.D.

Obsah CD:
    - /src/impl - adresar se zdrojovym kodem nastroje
    - /src/thesis - adresar se zdrojovymi kody práce ve formatu LaTeX
    - /text - adresar s textem prace ve formatu pdf
    
Instalace a spusteni nastroje:
    Potrebne nastroje:
          - Maven (verze 3.3.3+)
          - Java 1.8
          - Pro spusteni testu nad databazi, je potreba instalace databaze 
            Oracle Database 11g Release 2 a pote pripadne zmenit udaje k pripo-
            jeni v souboru DatabaseUtilsTest.java
            OJDBC driver musi byt v pocitaci nainstalovan, pripadne je potreba
            jej odebrat ze souboru pom.xml
            
    Instalace:
          - Instalace OJDBC do Maven viz 
            "https://www.mkyong.com/maven/how-to-add-oracle-jdbc-driver-in-your-maven-local-repository/"
          - Pote ve slozce /src/impl spuste prikaz "mvn clean install" 
    
    Spusteni:
          - Skrz vyvojove prostredi a Spring Boot plugin
          - Nebo ve slozce /src/impl prikazem "mvn spring-boot:run"