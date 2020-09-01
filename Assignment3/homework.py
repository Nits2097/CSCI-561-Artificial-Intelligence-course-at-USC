import copy
kb=[]
pred = []
clauses=[]
left=[]
yoohoo=0

def main():
    filename = 'input.txt'
    file =  open(filename, "r")  
    data = file.readlines() 
    noOfQueries = int(data[0])
    query = []
    for i in range(noOfQueries):
    	s = data[i+1].rstrip()
    	query.append(s)
    noOfFacts = int(data[i+2])
    fact = []
    for i in range(noOfFacts):
    	t = data[noOfQueries + 2 + i].rstrip()
    	fact.append(t)
   
    for i in fact:
    	convertToCNF(i)
    for i in query:
    	output = transformQuery(i)
    	writeOutput(output)
 


def resolvekb(query):

	flag=0
	count=0
	remove=0
	resolvent=[]
	for a in query:
		yoohoo=0
		for k in clauses:
			for l in k:
				yoohoo=0
				if(a!=k and l[0]==a[0] and not l[1]==a[1]):
					flag,resolvent = unify([a],k)
					if(flag):
						yoohoo=1
						flag=0
						if(resolvent):
							val = resolvekb(resolvent)
							if(not val):
								return False
							flag=1

			
		if(yoohoo==0):
			return False
		
	return True
	
			
def writeOutput(output):
	name = "output.txt"
	file = open(name,"a")
	file.write(str(output).upper() + "\n")

	

def unify(ca,cb):
	
	temp1 = copy.deepcopy(ca)
	temp2 = copy.deepcopy(cb)
	fin=[]
	subs={}
	flag=0
	for i in range(len(ca)):
		for k in range(len(cb)):
			if(ca[i][0]==cb[k][0] and ca[i][1]!=cb[k][1]):
				flag,subs = unifyVar(subs,cb[k][2],ca[i][2])
				if(flag):
					break
		if(flag):
			break
	if(flag):
		for i in range(len(temp1)):
			for j in range(len(temp1[i][2])):
				if(temp1[i][2][j] in subs):
					temp1[i][2][j] = subs[temp1[i][2][j]]
			fin.append(temp1[i])
		for i in range(len(temp2)):
			for j in range(len(temp2[i][2])):
				if(temp2[i][2][j] in subs):
					temp2[i][2][j] = subs[temp2[i][2][j]]

			temp2[i][1] = int(not temp2[i][1])
			if(temp2[i] not in fin):
				temp2[i][1] = int(not temp2[i][1])
				if(temp2[i] not in fin):
					fin.append(temp2[i])
			else:
				fin.remove(temp2[i])
				temp2[i][1] = int(not temp2[i][1])
		if(fin):
			clauses.append(fin)
		
		return 1,fin
	return 0,fin

def unifyVar(subs,arg1,arg2):
	for i in range(len(arg1)):
		if(arg1[i][0].islower() and arg2[i][0].isupper()):
			if arg1[i][0] not in subs:
				subs[arg1[i]] = arg2[i]
			elif(subs[arg1[i]]!=arg2[i]):
				subs.clear()
				return 0,subs
		elif(arg2[i][0].islower() and arg1[i][0].isupper()):
			if arg2[i] not in subs:
				subs[arg2[i]] = arg1[i]
			elif(subs[arg2[i]]!=arg1[i]):
				subs.clear()
				return 0,subs
		elif(arg1[i][0].islower() and arg2[i][0].islower() and arg1[i][0]!=arg2[i][0]):
			if (arg2[i] and arg1[i]) not in subs:
				subs[arg2[i]] = arg1[i]
			elif(arg1[i] in subs and arg2[i] not in subs):
				subs[arg2[i]] = subs[arg1[i]]
			elif(arg2[i] in subs and arg1[i] not in subs):
				subs[arg1[i]] = subs[arg2[i]]
			elif(subs[arg1[i]]!=subs[arg2[i]]):
				subs.clear()
				return 0,subs
		else:
			if(arg1[i]!=arg2[i]):
				subs.clear()
				return 0,subs
	return 1,subs


def transformQuery(fact):
	
	if(fact[0]=="~"):
		fact = fact[1:]
	else:
		fact = "~" + fact
	kb.append(fact)
	tup = makeClauses(fact)
	clauses.insert(0,[tup])
	standardizeKB()
	fin = resolvekb([tup])
	return fin

def storePredicates(fact):
	neg=0
	bracSt = fact.find("(")
	bracEnd = fact.find(")")
	brac= fact[bracSt,bracEnd+1]
	if(fact[0]=="~"):
		fact = fact[1:bracSt]
		neg = 1
	else:
		fact = fact[:bracSt]
	args= []
	commaInd = brac.find(",")
	

def makeClauses(fact):
	args=[]
	bracSt = fact.find("(")
	bracEnd = fact.find(")")
	brac= fact[bracSt+1:bracEnd]
	commaInd = brac.find(",")
	while(commaInd!=-1):
		args.append(brac[:commaInd])
		brac = brac[commaInd+1:]
		commaInd=brac.find(",")
	args.append(brac)
	if(fact[0]=="~"):
		neg = 1
		pred = fact[1:bracSt]
	else:
		neg=0
		pred = fact[:bracSt]
	tup = [pred,neg,args];
	return tup

def convertToCNF(fact):
	index = fact.find("=>")
	if(index==-1):
		tup = makeClauses(fact)
		clauses.append([tup])
		kb.append(fact) 
	else:
		cl = []
		premise = fact[:index-1]
		conclu = fact[index+3:]
		andInd = premise.find("&")
		first=""		
		while(andInd!=-1):
			part = premise[:andInd-1]
			premise = premise[andInd+2:]
			if(part[0]=="~"):
				first = first + "|" + part[1:]
				cl.append(makeClauses(part[1:]))
			else:
				first = first + "|~" + part
				cl.append(makeClauses("~" + part))
			andInd=premise.find("&")
		if(premise[0]=="~"):
			first = first + "|" +  premise[1:]
			cl.append(makeClauses(premise[1:]))
		else:
			first = first + "|~" + premise
			cl.append(makeClauses("~" + premise))
		first = first + "|" + conclu
		cl.append(makeClauses(conclu))
		clauses.append(cl)
		first = first[1:]
		kb.append(first)


def standardizeKB():
	ind=0 
	sind=1
	s="abcdefghijklmnopqrstuvwxyz"
	for i in range(len(clauses)):
		dict={}
		for j in range(len(clauses[i])):
			for arg in range(len(clauses[i][j][2])):
				#print(dict)
				temp = clauses[i][j][2][arg]
				if(temp[0].islower() and not temp in dict):
					if(ind<=25):
						dict[temp] = s[ind]
						clauses[i][j][2][arg] = s[ind]
						ind=ind+1
					elif(ind>25):
						if(ind>103):
							sind=4
						elif(ind>77):
							sind=3
						elif(ind>51):
							sind=2
						dict[temp] = s[sind-1] + s[ind-(26*sind)]
						clauses[i][j][2][arg] = s[sind-1] + s[ind-(26*sind)]
						ind=ind+1
				elif(temp in dict):
					clauses[i][j][2][arg] = dict[temp]

    
if __name__ == '__main__':
    main()       
