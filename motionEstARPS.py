import numpy


def costFuncMAD(two_d_arr_1, two_d_arr_2, mbSize):
    ...


def motionEstARPS(imgP: list, imgI: list, mbSize: int, p: int):
    # imgP, ImgI example: 200x300 list
    row, col = len(imgI), len(imgI[0])
    #vectors = numpy.zeros(2, row*col//mbSize**2)
    vectors = [[0 for _ in range(row*col//mbSize**2)] for _ in range(2)]
    # vectors: [2][1100]
    #costs = numpy.ones(1, 6) * 65537
    costs = [65537 for _ in range(6)]
    # costs: [6]
    # The index points for Small Diamond search pattern
    SDSP = [[0, -1], [-1, 0], [0, 0], [1, 0], [0, 1]]
    LDSP = [[0, 0], [0, 0], [0, 0], [0, 0], [0, 0]]
    # We will be storing the positions of points where the checking has been
    # already done in a matrix that is initialised to zero. As one point is
    # checked, we set the corresponding element in the matrix to one.
    #checkMatrix = numpy.zeros(2*p+1, 2*p+1)
    checkMatrix = [[0 for _ in range(2*p+1)] for _ in range(2*p+1)]
    # checkMatrix: [2p+1][2p+1]
    computations = 0

    # we start off from the top left of the image
    # we will walk in steps of mbSize
    # mbCount will keep track of how many blocks we have evaluated
    mbCount = 1
    for i in range(0, row-mbSize, mbSize):
        for j in range(0, col-mbSize, mbSize):
            # the Adaptive Rood Pattern search starts
            # we are scanning in raster order
            x = j
            y = i

            #costs = costFuncMAD(imgP[i:i+mbSize-1][j:j+mbSize-1], imgI[i:i+mbSize-1][j:j+mbSize-1], mbSize)
            costs[2] = costFuncMAD(
                imgP[i:i+mbSize][j:j+mbSize], imgI[i:i+mbSize][j:j+mbSize], mbSize)
            checkMatrix[p][p] = 1
            computations += 1
            # if we are in the left most column then we have to make sure that
            # we just do the LDSP with stepSize = 2
            if (j-1 < 1):
                stepSize = 2
                maxIndex = 4
            else:
                stepSize = max(
                    abs(vectors[0][mbCount-1]), abs(vectors[1][mbCount-1]))
                # now we have to make sure that if the point due to motion
                # vector is one of the LDSP points then we dont calculate it again
                if (abs(vectors[0][mbCount-1]) == stepSize and abs(vectors[1][mbCount-1]) == 0) or (abs(vectors[1][mbCount-1]) == stepSize and abs(vectors[0][mbCount-1]) == 0):
                    maxIndex = 4  # we just have to check at the rood pattern 5 points
                else:
                    maxIndex = 5  # we have to check 6 points
                    LDSP.append([vectors[1][mbCount-1],  vectors[0][mbCount-1]])

            LDSP[0] = [0, -stepSize]
            LDSP[1] = [-stepSize, 0]
            LDSP[2] = [0, 0]
            LDSP[3] = [stepSize, 0]
            LDSP[4] = [0, stepSize]

            for k in range(maxIndex):
                # row/Vert co-ordinate for ref block
                refBlkVer = y + LDSP[k][1]
                refBlkHor = x + LDSP[k][0]   # col/Horizontal co-ordinate
                if (refBlkVer < 1 or refBlkVer+mbSize > row or refBlkHor < 1 or refBlkHor+mbSize > col):
                    continue  # outside image boundary
                if (k == 2 or stepSize == 0):
                    continue  # center point already calculated
                costs[k] = costFuncMAD(
                    imgP[i:i+mbSize][j:j+mbSize], imgI[refBlkVer:refBlkVer+mbSize][refBlkHor:refBlkHor+mbSize], mbSize)
                computations += 1
                checkMatrix[LDSP[k][1] + p][LDSP[k][0] + p] = 1
            
            cost, point = min(costs), costs.index(min(costs))
            # The doneFlag is set to 1 when the minimum is at the center of the diamond           
            x = x + LDSP[point][0]
            y = y + LDSP[point][0]
            costs = [65537 for _ in range(5)]
            costs[2] = cost

            doneFlag = False
            while not doneFlag:
                for k in range(5):
                    refBlkVer = y + SDSP[k][1]   # row/Vert co-ordinate for ref block
                    refBlkHor = x + SDSP[k][0]   # col/Horizontal co-ordinate
                    if ( refBlkVer < 1 or refBlkVer+mbSize > row or refBlkHor < 1 or refBlkHor+mbSize > col):
                      continue
                    if (k == 2):
                        continue
                    elif (refBlkHor < j-p or refBlkHor > j+p or refBlkVer < i-p or refBlkVer > i+p):
                        continue
                    elif (checkMatrix[y-i+SDSP[k][1]+p][x-j+SDSP[k][0]+p] == 1):
                        continue
                    costs[k] = costFuncMAD(imgP[i:i+mbSize][j:j+mbSize],imgI[refBlkVer:refBlkVer+mbSize][refBlkHor:refBlkHor+mbSize], mbSize)
                    checkMatrix[y-i+SDSP[k][1]+p][x-j+SDSP[k][0]+p] = 1
                    computations += 1
                cost, point = min(costs), costs.index(min(costs))
                if (point == 2):
                    doneFlag = True
                else:
                    x = x + SDSP[point][0]
                    y = y + SDSP[point][1]
                    costs = [65537 for _ in range(5)]
                    costs[2] = cost
            vectors[0][mbCount] = y - i    # row co-ordinate for the vector
            vectors[1][mbCount] = x - j    # col co-ordinate for the vector            
            mbCount = mbCount + 1
            costs = [65537 for _ in range(6)]
            checkMatrix = [[0 for _ in range(2*p+1)] for _ in range(2*p+1)]

    motionVect = vectors
    ARPScomputations = computations/(mbCount-1)
    return motionVect, ARPScomputations