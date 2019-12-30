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
    #print("FACTS")
    #print(fact)
    #print("QUERY")
    #print(query)
    #can query contain multiple predicates or not
    for i in fact:
    	convertToCNF(i)
    print("YEH HAI clauses")
    print(clauses)
    for i in query:
    	output = transformQuery(i)
    	print("output")
    	writeOutput(output)
    #break
    #removeExtraClauses() 
    #print("YEH HAI KB")
    #print(kb)
    #standardizeKB()


def resolvekb(query):
	print("CALLED AGAIN WITH QUERY AS")
	print(query)

	flag=0
	count=0
	remove=0
	resolvent=[]
	#print(query)
	for a in query:
		yoohoo=0
		print("ITERATIONNNNNN")
		print(count)
		print(a)
		for k in clauses:
			print("idhar aaya")
			print(k)
			for l in k:
				yoohoo=0
				print("VALUE OF L")
				print(l)
				if(a!=k and l[0]==a[0] and not l[1]==a[1]):
					print("ab unify karo inko")
					flag,resolvent = unify([a],k)
					if(flag):
						yoohoo=1
						flag=0
						if(resolvent):
							val = resolvekb(resolvent)
							if(not val):
								return False
							flag=1
						#break

			#if(remove==1):
			#	break
			#if(flag):
			#	break
			# if(not resolvent and flag):
		  	#count=count+1
		 	#continue
		if(yoohoo==0):

			print("YEH QUERY KA KUCH NAHI HUA")
			return False
		#if(k==len(clauses)):
		#	return False
		# if(flag):
		#  	if(not resolvent):
		#  		count=count+1
		#  	continue
		#if(not resolvent and flag):
		 	#count=count+1
		 	#continue
	return True
	# print("done.done.")
	# if(yoohoo==1):
	# 	#print("okay.")
	# 	return True
	# else:
	# 	#print("not okay.")
	# 	return False
	#if(count==len(query)):
	#	print("trueeeee hua resolve")
	#	return True
	#else:
	#	print("FALSEEEEEEEEEEEE NAHI HUA RESOLVE")
	#return True
			
def writeOutput(output):
	name = "output.txt"
	file = open(name,"a")
	file.write(str(output).upper() + "\n")

	

def unify(ca,cb):
	print("unifying")
	print("CA")
	print(ca)
	print("CB")
	print(cb)
	temp1 = copy.deepcopy(ca)
	temp2 = copy.deepcopy(cb)
	fin=[]
	subs={}
	flag=0
	for i in range(len(ca)):
		for k in range(len(cb)):
			if(ca[i][0]==cb[k][0] and ca[i][1]!=cb[k][1]):
				flag,subs = unifyVar(subs,cb[k][2],ca[i][2])
				print("aya")
				#print(flag,subs)
				if(flag):
					break
		if(flag):
			break
	if(flag):
		print("byeeeeeeeeeeeeeeeeeeeeyb")
		print(subs)
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
		print("FIN KA VALUEEEEEE")
		print(fin)
		print("clauses ab after unify")
		print(clauses)
		return 1,fin
	return 0,fin

def unifyVar(subs,arg1,arg2):
	#subs={}
	print(subs)
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
	# assumed query is a single predicate
	# what if its multiple predicate?
	if(fact[0]=="~"):
		fact = fact[1:]
	else:
		fact = "~" + fact
	# print(fact)
	# print("fact getting appended")
	kb.append(fact)
	tup = makeClauses(fact)
	clauses.insert(0,[tup])
	standardizeKB()
	fin = resolvekb([tup])
	print("done done done done")
	print(fin)
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
	# will we store 4th argument as others with the predicat in the kb?
	# also how, if we have aVb, ~avbv~d, 

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
					#print("idhar aaye okay!")
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
	print("clauses ab")
	print(clauses)

    
if __name__ == '__main__':
    main()       
