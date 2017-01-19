# FSPtoPetriNet
This project is a visualization tool to convert an FSP specification to a Petri Net

#Pre-Requisites

1. Exclipse
2. GraphViz tool
3. Python

# To Run on Eclipse

1. Open Eclipse
2. Click on File -> Import
3. Choose General -> Existing Projects into Workspace
4. Select the lta folder as root directory
5. Click on Projects -> Properties
6. Choose the Java Build Path tab
7. In Libraries add ant_parser.jar, jel.jar, LTL2Buchi.jar
8. In Source tab add the following folder, lib/scenebeans-master/src/main/java
9. Run Project as Java Application with HPWindow.class as main class
10. After viewing, the LTS drawn by any FSP, run parser.py
11. To draw the Petri Net and View it, run the following commands:
```
dot -T png graph.gv -o output
xdg-open output
```
