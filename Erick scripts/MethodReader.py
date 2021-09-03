import os
import csv

''' Python file intended to be manually run with gathered data 
    from 'MethodsInvoked.txt' files
'''

InvokedMethods = [];
TotalInvokes = 0;

#2D Array of project[] objects, project[0] = Project name, project[1] = All files, 
#   project[2] = Test files, project[3] = Git Author commits, project[4] = Git Tag files,
#   project[5] = Mock imports in Test files, project[6] = Mock frameworks in Test files

nonProjectFiles = []
# Set files that MethodReader should ignore here.

def MethodsInvoked (projectName):  
    filePath ="UndProjects/" + projectName + " - MethodsInvoked.txt"
    try:
#        print(filePath)
        reader = open(filePath, encoding= 'utf-8')
    except FileNotFoundError:
        print(projectName + " is missinag a 'MethodsInvoked.txt'.")
        return []
    else:
        reader = open(filePath,encoding= 'utf-8')
        
    Invokes = []
    currentFile = ""
    projectInvokes = 0
    totalInvokes = 0
    
    for line in reader:
        line = line.strip()
        if line[0:4].lower() == "file":
            currentFile = line[line.index(":") + 2:]
        if line[0:6].lower() == "method":
            totalInvokes = int(line[line.index("(") + 1:line.index(")")]);  
            projectInvokes += totalInvokes
        if line[0:7].lower() == "invoked":
            method = line[line.rindex(".") + 1:];
            framework = IdentifyMockFramework(line[line.index(":") + 2:].lower())
            invokeCnt = int(line[line.index("(") + 1:line.index(")")]);
            Invokes.append([projectName,currentFile,framework,method,invokeCnt,round((invokeCnt/totalInvokes),4) * 100])

    return [Invokes,projectInvokes]

def FrameworksInvoked(methodsInvokedList):
    Frameworks = {};
    for entry in methodsInvokedList:
        framework = entry[2]
        if framework not in Frameworks:
            Frameworks[framework] = 0
        Frameworks[framework] += entry[4]
    totalMockInvokes = 0;
    for framework in Frameworks:
        totalMockInvokes += Frameworks[framework]
    return [Frameworks,totalMockInvokes]

def IdentifyMockFramework(invokeLine):
    ''' Trims/reads a line that is invoking an outside mocking 
        framework to identify the framework used.
        invokeLine (string): Raw line of code that invokes a mocking tool.
    '''
    invokeLine = invokeLine.strip().lower()
    
    #Check/Handling for Static, which does not follow the "xxx.xxxx.xxxx." format.
    if invokeLine[0:6].lower() == 'static':
        invokeLine = invokeLine[7:]
    
    #If a 'xxx.' section matches one of these, it is a prefix to the framework invoke and can be removed.
    nonFrameworkPrefixes = ['okhttp3','org','io','com','compile','google','api','net','jvnet','javacrumbs']
    
    validLine = False
    while not validLine:
        validLine = True
        nextPeriod = invokeLine.index(".")
        
        #Check/Handling for Github invokes, which include the name of the repo owner afterwards.
        if invokeLine[:nextPeriod] == "github":
            invokeLine = invokeLine[nextPeriod + 1:]
            nextPeriod = invokeLine.index(".")
            invokeLine = invokeLine[nextPeriod + 1:]
            validLine = False

        if invokeLine[:nextPeriod] in nonFrameworkPrefixes:
            invokeLine = invokeLine[nextPeriod + 1:]
            validLine = False
        
    return invokeLine[:nextPeriod]

def SortByMockFramework(methodsInvokedList,framework):
    
    sortedInvokedList = {}
    totalInvoked = 0
    for entry in methodsInvokedList:
        if entry[2] == framework:
            if entry[3] not in sortedInvokedList:
                sortedInvokedList[entry[3]] = 0
            sortedInvokedList[entry[3]] += entry[4]
            totalInvoked += entry[4]
    return [sortedInvokedList, totalInvoked]


for file in os.listdir('UndProjects'):
    if '.txt' in file and file not in nonProjectFiles:
        project = file[:-21]
        print (file)
        results = MethodsInvoked(project)
        InvokedMethods += results[0]
        TotalInvokes += results[1]

tempResult = FrameworksInvoked(InvokedMethods);
FrameworkMethods = tempResult[0]
TotalMockInvokes = tempResult[1]

FrameworkMethodsByFramework = {}
for framework in FrameworkMethods:
    FrameworkMethodsByFramework[framework] = (SortByMockFramework(InvokedMethods,framework))


print();        
print('Writing to "UndProjects/AllMockRatios"')
reader = open('UndProjects/AllMockRatios.csv','w',newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name','File','Framework','Mock Method','# of Invocations','% of Invocations'])

for entry in InvokedMethods:
    name = entry[0]
    filePath = entry[1]
    framework = entry[2]
    mockMethod = entry[3]
    numInvo = entry[4]
    perInvo = entry[5]
    csvOutput.writerow([name,filePath,framework,mockMethod,numInvo,perInvo])

reader.close()

print('Writing to "UndProjects/AllFrameworkRatios"')
reader = open('UndProjects/AllFrameworkRatios.csv','w',newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Framework','# of Invocations','% of All Test File Invocations','% of All Mock Invocations'])

for framework in FrameworkMethods:
    numInvokes = FrameworkMethods[framework]
    csvOutput.writerow([framework,numInvokes,round((numInvokes/TotalInvokes),4) * 100,round((numInvokes/TotalMockInvokes),4) * 100])    

reader.close()


for framework in FrameworkMethods:
    
    print('Writing to "UndProjects/PerFrameworkData/' + framework + ' - Ratios"')
    reader = open('UndProjects/PerFrameworkData/' + framework + ' - Ratios.csv','w',newline = '')

    csvOutput = csv.writer(reader)
    csvOutput.writerow(['Mock Method','# of Invocations','% of Framework Invocations'])

    tempResults = SortByMockFramework(InvokedMethods,framework)
    currentSortedInvokes = tempResults[0]
    currentTotalInvokes = tempResults[1]
    for entry in currentSortedInvokes:
        csvOutput.writerow([entry,currentSortedInvokes[entry],round(currentSortedInvokes[entry]/currentTotalInvokes,4) * 100])
    

    


print ('-- MethodReader.py Complete --')


        


    

