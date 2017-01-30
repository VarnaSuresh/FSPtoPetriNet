import re

#Read the tansitions from file derived from LTSA parser

with open ('output.txt', 'r') as fp:
    lineList = fp.read().splitlines();
	
lineList = list(set(lineList))	

#Differentiate transitions if they come from two different places
print lineList 


data = []
with open("fdp-test1.gv", 'w') as fp:
	data = data + ["digraph G {\n start; \n end; "];

	count1 = 0

	for line in lineList:
		count1 = count1 + 1

	count1 = count1/2;

	for linei in range(0,count1):
		data = data + ["subgraph cluster"+ str(linei + 1) + "{\nsubgraph transitions{","node [shape=rect,height=0.2,width=2];","temp" + str(linei + 1) +";","}","}"]
	
	for linei in range(0,count1):	
		if linei == 0:
			data = data + ["start->cluster"+str(linei+1)+";\n"]
		if linei == count1-1:
			data = data + ["cluster"+str(linei+1)+"->"+"end;\n}"]	
		else:
			data = data + ["cluster"+str(linei+1)+"->cluster"+ str(linei + 2) +";\n"]
	
	for line in data:
		fp.writelines(line + "\n")


'''
print itemList 


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
with open("fdp-test1.gv", 'w') as fp:
	data = data + ["digraph G {\n start; \n end; " ,"subgraph cluster1 {","subgraph transitions{","node [shape=rect,height=0.2,width=2];","temp;","}","}"];
	
	#data = data + ["} "]

	for line in data:
		fp.writelines(line + "\n")



	for line in lineList:
		items = filter(None, re.split(" ", line))
		data = data + [items[0] + "->" + items[1] + ";", items[1] + "->" + items[2] + ";"]
	
	#Remove redundant transitions

	from  more_itertools import unique_everseen
	data = list(unique_everseen(data))

	data = data + ["}"]

	#Write to dot file

		
'''
	