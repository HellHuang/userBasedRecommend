__author__ = 'fen'
#coding=utf-8
import math
from scipy import stats
import numpy as np

maxuser=5
simMatrix=[[0.0 for i in range(maxuser) ]for j in range(maxuser)]
m=[[5,3,4,4,-1],[3,1,2,3,3],[4,3,4,3,5],[3,3,1,5,4],[1,5,5,2,1]] #没有评分的设为-1
avg_user=[0 for i in range(maxuser)]
for i in range(maxuser):
    avg_i=0
    count=0
    for j in range(maxuser):
        if m[i][j]!=-1:
            avg_i += m[i][j]
            count+=1
    avg_user[i] = avg_i/float(count)
print avg_user


for user_i in range(maxuser):

    for user_j in range(maxuser):
        # up=0
        # down=0
        # done=0
        # dtwo=0
        # for item in range(m[0].__len__()):
        #
        #     if m[i][item]!=-1 and m[j][item]!=-1 and i!=j:
        #         up+=(m[i][item]-avg_user[i])*(m[j][item]-avg_user[j])
        #         done+=((m[i][item]-avg_user[i])**2)
        #         dtwo+=((m[j][item]-avg_user[j])**2)
        # down=math.sqrt(done)*math.sqrt(dtwo)
        # print down
        # if down != 0:
        #     simMatrix[i][j]=up/(float(down))
        '''if i == 0 or j == 0:
            tmp_i = np.array(m[i][:4])
            tmp_j = np.array(m[j][:4])'''


        for item in range(m[0].__len__()):
            temp_i=[]
            temp_j=[]
            if m[user_i][item]==-1 or m[user_j][item]==-1 :
                temp_i +=m[user_i][:item]
                temp_j +=m[user_j][:item]

            else:
                temp_i = m[user_i]
                temp_j = m[user_j]
            temp_i=np.array(temp_i)
            temp_j=np.array(temp_j)

            simMatrix[user_i][user_j] = stats.pearsonr(temp_i, temp_j)[0]

for i in range(maxuser):
    print simMatrix[i]

    #numpy