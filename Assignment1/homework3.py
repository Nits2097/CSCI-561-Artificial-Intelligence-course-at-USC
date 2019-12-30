from operator import itemgetter


def traverseInput(filename):
    file =  open(filename, "r")  
    data = file.readlines() 
    algo = (data[0])
    w,h = data[1].split()
    thresh = int(data[3])
    startx, starty = data[2].split()
    startx, starty = int(startx),int(starty)
    w,h = int(w),int(h)
    target = []
    copytar = []
    no_of_targets = int(data[4])
    for i in range(no_of_targets):
      target.append(data[i+5])    
      copytar.append(data[i+5])
    a = [['0' for i in range(w)] for j in range(h)]
    for i in range(h):
        a[i] = data[i+5 + no_of_targets].split()
    for i in range(h):
      for j in range(w):
        a[i][j] = int(a[i][j])
    file.close()
    return (algo,w,h,startx,starty,target,a,thresh,copytar)

def findNeighbors(w,h,ele,a,thresh):
    x,y = ele
    n=[]
    cy,dy,dx,cx,y1,y2,x1,x2 = 0,0,0,0,0,0,0,0
    if((y+1)!=h):
        cy=1
    if((y-1)!=h):
        dy=1
    if((x-1)!=w):
        dx=1
    if((x+1)!=w):
        cx=1
    if((y+1)!=-1):
        y1=1
    if((y-1)!=-1):
        y2=1
    if((x+1)!=-1):
        x1 = 1
    if((x-1)!=-1):
        x2 = 1
    
    if(y1 and cy and (abs(a[y+1][x] - a[y][x]) <= thresh)):
        n.append((x,y+1))

    if(y1 and cy and x1 and cx and (abs(a[y+1][x+1] - a[y][x]) <= thresh)):
        n.append((x+1,y+1))

    if(x1 and cx) and (abs(a[y][x+1] - a[y][x]) <= thresh):
        n.append((x+1,y))

    if(y2 and dy and x2 and dx) and (abs(a[y-1][x-1] - a[y][x]) <= thresh):
        n.append((x-1,y-1))

    if(y2 and dy and x1 and cx) and (abs(a[y-1][x+1] - a[y][x]) <= thresh):
        n.append((x+1,y-1))

    if(y2 and dy) and (abs(a[y-1][x] - a[y][x]) <= thresh):
        n.append((x,y-1))

    if(x2 and dx) and (abs(a[y][x-1] - a[y][x]) <= thresh):
        n.append((x-1,y))

    if(y1 and cy and x2 and dx and (abs(a[y+1][x-1] - a[y][x]) <= thresh)):
        n.append((x-1,y+1))   
    return n

def checkTargets(w,h, target):
    temp = []
    l = len(target)
    for i in target:
        c,d = i.split()
        if(int(c)>=w or int(d)>=h):
            temp.append(i)
    for i in temp:
        target.remove(i)
    return target

def goalTest(ele,target):
    x,y = ele   
    for i in target:
        c,d = i.split()
        if(x==int(c) and y==int(d)):
            target.remove(i)
            return True
    return False 

    
def writeSolution(target,finalAns,parent):
    name = "output.txt"
    file = open(name,"w")
    for i in target[:-1]:
        stack=[]
        x,y = i.split()
        x = int(x)
        y=  int(y)
        tar1 = x
        tar2 = y
        eachPath = ""
        if ((x,y)) in finalAns.keys():
            while(parent[(x,y)]!="start"):
                stack.append(parent[(x,y)])
                x,y = parent[(x,y)]
            stack.reverse()
            for a,b in stack:
                eachPath = eachPath + str(a) + "," + str(b) + " "
            eachPath = eachPath + str(tar1) + "," + str(tar2)
            file.write(eachPath + "\n")
        else:
            file.write("FAIL\n")
    stack=[]
    i = target[-1]
    x,y = i.split()
    x,y = int(x), int(y)
    tar1, tar2 = x,y
    eachPath = ""
    if ((x,y)) in finalAns.keys():
        while(parent[(x,y)]!="start"):
            stack.append(parent[(x,y)])
            x,y = parent[(x,y)]
        stack.reverse()
        for a,b in stack:
            eachPath = eachPath + str(a) + "," + str(b) + " "
        eachPath = eachPath + str(tar1) + "," + str(tar2)
        file.write(eachPath)
    else:
        file.write("FAIL")     
    file.close()    
    
def findSolutionBFS(x,y,w,h,a,thresh,target,copytar):
    target = checkTargets(w,h,target)
    visited = [[0 for i in range(w)] for j in range(h)]
    visited[y][x] = 1
    queue = []
    finalAns = {}
    targetCounter = len(target)
    parent = {}
    parent[(x,y)] = "start"
    queue.append((x,y))
    while(len(queue)!=0 and targetCounter!=0):
        ele = queue[0]
        if(goalTest(ele,target)):
            finalAns[ele] = parent[ele]
            targetCounter = targetCounter - 1
            if(targetCounter==0):
                break
        n = findNeighbors(w,h,ele,a,thresh)
        del(queue[0])
        for ij,ik in n:
            if((ij,ik) not in queue and visited[ik][ij]!=1):
                visited[ik][ij] = 1
                queue.append((ij,ik))
                parent[(ij,ik)] = ele
    writeSolution(copytar,finalAns,parent)

def expandUCS(ele,initCost,w,h,a,thresh):   
    x,y = ele
    n=[]
    cy,dy,dx,cx,y1,y2,x1,x2 = 0,0,0,0,0,0,0,0
    if((y+1)!=h):
        cy=1
    if((y-1)!=h):
        dy=1
    if((x-1)!=w):
        dx=1
    if((x+1)!=w):
        cx=1
    if((y+1)!=-1):
        y1=1
    if((y-1)!=-1):
        y2=1
    if((x+1)!=-1):
        x1 = 1
    if((x-1)!=-1):
        x2 = 1
    strCost = 10 + initCost
    diagCost = 14 + initCost
    if(x1 and cx) and (abs(a[y][x+1] - a[y][x]) <= thresh):
        n.append([(x+1,y),strCost])
        
    if((x1 and cx and y1 and cy) and abs(a[y+1][x+1] - a[y][x]) <= thresh):
        n.append([(x+1,y+1),diagCost])

    if((y1 and cy) and (abs(a[y+1][x] - a[y][x]) <= thresh)):
        n.append([(x,y+1),strCost])

    if((x2 and dx and y2 and dy) and (abs(a[y-1][x-1] - a[y][x]) <= thresh)):
        n.append([(x-1,y-1),diagCost])

    if((x2 and dx and y1 and cy) and (abs(a[y+1][x-1] - a[y][x]) <= thresh)):
        n.append([(x-1,y+1),diagCost])

    if((x2 and dx) and (abs(a[y][x-1] - a[y][x]) <= thresh)):
        n.append([(x-1,y),strCost])

    if((y2 and dy) and (abs(a[y-1][x] - a[y][x]) <= thresh)):
        n.append([(x,y-1),strCost])

    if((x1 and cx and y2 and dy) and (abs(a[y-1][x+1] - a[y][x]) <= thresh)):
        n.append([(x+1,y-1),diagCost])              
    return n

def expandAstar(ele,initCost,w,h,a,thresh,target):
    x,y = ele
    n=[]

    cy,dy,dx,cx,y1,y2,x1,x2 = 0,0,0,0,0,0,0,0
    if((y+1)!=h):
        cy=1
    if((y-1)!=h):
        dy=1
    if((x-1)!=w):
        dx=1
    if((x+1)!=w):
        cx=1
    if((y+1)!=-1):
        y1=1
    if((y-1)!=-1):
        y2=1
    if((x+1)!=-1):
        x1 = 1
    if((x-1)!=-1):
        x2 = 1
    tar1, tar2 = target.split()
    if(initCost==0):
        hnSub = 0
    else:
        hnSub = abs(int(tar1) - x) + abs(int(tar2) - y) 
    strCost = 10 + initCost - hnSub
    diagCost = 14 + initCost - hnSub
    
    if(x1 and cx and (abs(a[y][x+1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x+1)) + abs(int(tar2) - (y))
        add = abs(a[y][x+1] - a[y][x]) + hn
        n.append([(x+1,y),strCost+add])
        
    if(x1 and cx and y1 and cy and (abs(a[y+1][x+1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x+1)) + abs(int(tar2) - (y+1))
        add = abs(a[y+1][x+1] - a[y][x]) + hn
        n.append([(x+1,y+1),diagCost+add])

    if(y1 and cy and (abs(a[y+1][x] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x)) + abs(int(tar2) - (y+1))
        add = abs(a[y+1][x] - a[y][x]) + hn
        n.append([(x,y+1),strCost+add])

    if((x2 and dx and y2 and dy) and (abs(a[y-1][x-1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x-1)) + abs(int(tar2) - (y-1))
        add = abs(a[y-1][x-1] - a[y][x]) + hn
        n.append([(x-1,y-1),diagCost+add])

    if(x2 and dx and y1 and cy and (abs(a[y+1][x-1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x-1)) + abs(int(tar2) - (y+1))
        add = abs(a[y+1][x-1] - a[y][x]) + hn
        n.append([(x-1,y+1),diagCost+add])

    if(x2 and dx and (abs(a[y][x-1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x-1)) + abs(int(tar2) - (y))
        add = abs(a[y][x-1] - a[y][x]) + hn
        n.append([(x-1,y),strCost+add])

    if(y2 and dy and (abs(a[y-1][x] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x)) + abs(int(tar2) - (y-1))
        add = abs(a[y-1][x] - a[y][x]) + hn
        n.append([(x,y-1),strCost+add])

    if((x1 and cx and y2 and dy) and (abs(a[y-1][x+1] - a[y][x]) <= thresh)):
        hn = abs(int(tar1) - (x+1)) + abs(int(tar2) - (y-1))
        add = abs(a[y-1][x+1] - a[y][x]) + hn
        n.append([(x+1,y-1),diagCost+add])               
    return n

def findSolutionUCS(x,y,w,h,a,thresh,target,targetorg):
    target = checkTargets(w,h,target)
    visited = [[0 for i in range(w)] for j in range(h)]
    visited[y][x] = 1
    queue = []
    finalAns = {}
    targetCounter = len(target)
    parent = {}
    parent[(x,y)] = "start"
    queue.append([(x,y),0])
    while(len(queue)!=0 and targetCounter!=0):
        ele,cost = queue[0]
        if(goalTest(ele,target)):
            finalAns[ele] = parent[ele]
            targetCounter = targetCounter - 1
            if(targetCounter==0):
                break
        del(queue[0])
        n = expandUCS(ele,cost,w,h,a,thresh)
        for child,cos in n:    
            if(visited[child[1]][child[0]]==0):
                visited[child[1]][child[0]] = cos
                queue.append([child,cos])
                parent[child] = ele
                sortflag = 1
            else:
                for coor in queue:
                    if(child==coor[0] and cos<coor[1]):
                        coor[1] = cos
                        parent[child] = ele
                        break
        if(sortflag==1):
            queue.sort(key=itemgetter(1))
    writeSolution(targetorg,finalAns,parent)

def goalTestAstar(ele,i):
    x,y = ele
    c,d = i.split()
    if(x==int(c) and y==int(d)):
        return True
    return False  

def writeFail(i,count,l):
    if(count==l):
        s = "FAIL"
    else:
        s = "FAIL\n"
    file = open("output.txt","a")
    file.write(s)
    file.close()

def findPathAstar(parent,ele,count,l):
    tar1,tar2 = ele
    eleA,eleB = ele
    stack = []
    s= ""
    while(parent[(eleA,eleB)]!="start"):
        stack.append(parent[(eleA,eleB)])
        eleA,eleB = parent[(eleA,eleB)]
    stack.reverse()
    file = open("output.txt","a")
    eachPath = ""
    for a,b in stack:
        eachPath = eachPath + str(a) + "," + str(b) + " "
    eachPath = eachPath + str(tar1) + "," + str(tar2)
    if(count<l):
        file.write(eachPath + "\n")
    else:
        file.write(eachPath)  
    file.close()

def findSolutionAstar(x,y,w,h,a,thresh,target):
    file = open("output.txt","w")
    file.close()
    targetCounter = len(target)
    count=0
    for i in target:
        count=count+1
        c,d = i.split()
        if(int(c)>=w or int(d)>=h):
            if(count<len(target)):
                s = "FAIL\n"
            else:
                s = "FAIL"
            file = open("output.txt","a")
            file.write(s)
            file.close()
        else:
            queue = []
            visited = [[0 for m in range(w)] for n in range(h)]
            visited[y][x] = 1       
            parent = {}
            finalAns={}
            closed = []
            parent[(x,y)] = "start"
            queue.append([(x,y),0])
            sortflag = 0
            while(len(queue)!=0 and targetCounter!=0):
                ele,cost = queue[0]
                if(goalTestAstar(ele,i)):
                    findPathAstar(parent,ele,count,len(target))
                    goal=1
                    targetCounter = targetCounter - 1
                    break
                del(queue[0])
                n = expandAstar(ele,cost,w,h,a,thresh,i)
                for child,cos in n:    
                    if(visited[child[1]][child[0]]==0):
                        sortflag=1
                        queue.append([child,cos])
                        parent[child] = ele
                        visited[child[1]][child[0]] = cos
                    else:
                        for coor in queue:
                            if(child==coor[0] and coor[1]>cos):
                                coor[1] = cos
                                parent[child] = ele
                                break
                if(sortflag==1):                  
                    queue.sort(key=itemgetter(1))


def main():
    algo,w,h,x,y,target,a,thresh,copytar = traverseInput('in.txt')
    if(algo=="BFS\n"):          
        findSolutionBFS(x,y,w,h,a,thresh,target,copytar)    
    elif(algo=="UCS\n"):
        findSolutionUCS(x,y,w,h,a,thresh,target,copytar)
    elif(algo=="A*\n"):
        findSolutionAstar(x,y,w,h,a,thresh,target)
    
if __name__ == '__main__':
    main()       
