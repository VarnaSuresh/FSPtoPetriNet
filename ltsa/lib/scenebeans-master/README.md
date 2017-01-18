# SceneBeans for Maven [ ![Download](https://api.bintray.com/packages/lesunb/third-party/scenebeans/images/download.svg) ](https://bintray.com/lesunb/third-party/scenebeans/_latestVersion)
(forked from http://www-dse.doc.ic.ac.uk/Software/SceneBeans) 

An Animation Framework for Java

SceneBeans is a Java framework for building and controlling animated graphics.
It removes the drudgery of programming animated graphics, allowing programmers
to concentrate on what is being animated, rather than on how that animation is played back 
to the user. SceneBeans is based upon Java Beans and XML. 
Its component-based architecture allows application developers to easily extend the framework with 
domain-specific visual and behavioural components. It is used in the LTSA tool to animate formal models 
of concurrent systems and has also been used to build computer-based education programs for junior science.

## SceneBeans for Maven
* Just packaging it with maven. Refer to the original library page for documentation: 
http://www-dse.doc.ic.ac.uk/Software/SceneBeans/index.html

## Maven Dependency:
Include as dependency:

	  <dependencies>
		    <dependency>
		    	<groupId>uk.ac.ic</groupId>
		    	<artifactId>SceneBeans</artifactId>
		    	<version>1.0.0</version>
		    </dependency>
	  </dependencies>

And include our repository:

	<repositories> 
		  <repository>
		    <id>bintray-lesunb</id>
		    <url>https://dl.bintray.com/lesunb/third-party</url>
		  </repository>
	</repositories>
