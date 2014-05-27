Realtest
========

Framework End to End tests.

Official web site http://realtest.mycode.cc for all features and Screen Casts

This framework abstract the OS and Browser for you and provide infraestructure for you write tests in DSL expressive way.
You test is code and you like code. Promoting reuse of code and less boring test.

public void testTitleOfTutorialPage() {
		startFrom(RealTestHome.class).openHome().clickTutorial().assertThatTitleIs("1 - Instalando o TestNg no eclipse ");
	}

you can see fast just clone and 
mvn install test -Pfirefox
or 
mvn install test -Phtmlunit

in linux or windows.

For ie and chrome you need put in resources folder the driver.
