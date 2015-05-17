__author__ = 'fen'
#coding=utf-8
import math

def getUserItemMatrix(filepath):
    file=open(filepath)
    line=file.readline()
    maxuser=-1
    maxitem=-1
    matrix=[[-1 for i in range(6000)] for j in range(6000)]
    while line:
        line=line.strip().split(',')
        t1=int(line[0])
        t2=int(line[1])
        maxuser=max(maxuser,t1)
        maxitem=max(maxitem,t2)
        matrix[t1-1][t2-1]=int(line[2])
        line=file.readline()
    return matrix,maxuser,maxitem

def getPersonMatrix(m,maxuser):
    simMatrix=[[1 for i in range(maxuser) ]for j in range(maxuser)]
    avg_user=[0 for i in range(maxuser)]
    for i in range(maxuser):
        avg_i=0
        count=0
        for j in range(maxuser):
            if m[i][j]!=-1:
                avg_i += m[i][j]
                count+=1
        avg_user[i] = avg_i/float(count+0.001)

    for user_i in range(maxuser):

        for user_j in range(maxuser):
             up=0
             down=0
             done=0
             dtwo=0
             for item in range(m[0].__len__()):

                 if m[user_i][item]!=-1 and m[user_j][item]!=-1 and user_i !=user_j:
                     up+=(m[user_i][item]-avg_user[user_i])*(m[user_j][item]-avg_user[user_j])
                     done+=((m[user_i][item]-avg_user[user_i])**2)
                     dtwo+=((m[user_i][item]-avg_user[user_j])**2)
             down=math.sqrt(done)*math.sqrt(dtwo)

             if down != 0:
                 simMatrix[user_i][user_j]=up/(float(down*1.0))
    return avg_user,simMatrix

def pred(userItem,avg_user,simMatrix,maxuser,maxitem):
    pre=[[0 for i in range(maxuser)]for j in range(maxitem)]
    for user_a in range(maxuser):
        for item in range(maxitem):
            for user_b in range(maxuser):
                if simMatrix[user_a][user_b]!=-1:
                    pre[user_a][item]+= (simMatrix[user_a][user_b]*(userItem[user_b][item]-avg_user[user_b])/simMatrix[user_a][user_b])
        print user_a,item,pre[user_a][item]
    return pre
def rmse(testMatrix,preMatrix,maxuser,maxitem):
    count=0
    rmse=0
    for user in range(maxuser):
        for item in range(maxitem):
            if testMatrix[user][item]!=0:
                count+=1
                rmse+=((testMatrix[user][item]-preMatrix[user][item])**2)
    return math.sqrt(rmse/(count*1.0))

UserItemMatrix,maxuser,maxitem=getUserItemMatrix('trainningSet.txt')
'''for i in range(maxuser):
    for j in range(maxitem):
        if UserItemMatrix[i][j]!=-1:
            print UserItemMatrix[i][j]'''
avg_user,simMatrix=getPersonMatrix(UserItemMatrix,maxuser)
simFile=open("simMatrix.txt",'w')

userItem,k1,k2=getUserItemMatrix('testSet.txt')
for i in range(maxuser):
    for j in range(maxuser):
        simFile.write(str(simMatrix[i][j])+" ")
    simFile.write("\n")
simFile.close()

pre=pred(userItem,avg_user,simMatrix,maxuser,maxitem)
print "rmse",rmse(userItem,pre,maxuser,maxitem)