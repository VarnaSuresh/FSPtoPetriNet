import re

#Read the tansitions from file derived from LTSA parser

with open ('output.txt', 'r') as fp:
    lineList = fp.read().splitlines();
lineList = list(set(lineList))	

#Differentiate transitions if they come from two different places
print lineList

for i in range(len(lineList)):
	list1 = filter(None, re.split(" ", lineList[i]))
	print list1
	count = 48
	for j in range(i + 1, len(lineList)):
		list2 = filter(None, re.split(" ", lineList[j]))
		print list2
		if list1[1] == list2[1] and list1[2] != list2[2]:
				lineList[j] = list2[0] + " " + list2[1] + chr(count) + " " + list2[2]
				count += 1


#Create a list of all unique places and transitions 

itemList = []
for l in lineList:
 	l = re.split(' ', l)
 	itemList = itemList + filter(None, l)
itemList = list(set(itemList))	

#Define the places and transitions in the graph

data = []
with open("graph.gv", 'w') as fp:
	data = data + ["digraph G {","subgraph place {","graph [shape=circle,color=gray];","node [shape=circle,fixedsize=true,width=2];"];
	for item in itemList:
		if item == "0":
			data = data + [item + " [image=\"dot .png\" label=\"\n\n\n" + item + "\"];"]
		if item.isdigit():
			data = data + [item + ";"]
	data = data + ["}","subgraph transitions {","node [shape=rect,height=0.2,width=2];"]
	for item in itemList:
		if any(c.islower() for c in item):
			data = data + [item + ";"]
	data = data + ["} "]

	for line in lineList:
		items = filter(None, re.split(" ", line))
		data = data + [items[0] + "->" + items[1] + ";", items[1] + "->" + items[2] + ";"]
	
	#Remove redundant transitions

	from  more_itertools import unique_everseen
	data = list(unique_everseen(data))

	data = data + ["}"]

	#Write to dot file

	for line in data:
		fp.writelines(line + "\n")
