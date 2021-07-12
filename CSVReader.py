import os
import csv

''' Python file intended to be run in a batch script to read and write metrics 
    from CSV files generated from Understand Scitool projects.
'''

print('-- CSVReader.py loaded --')

Projects = []
#2D Array of project[] objects, project[0] = Project name, project[1] = All files, 
#   project[2] = Test files, project[3] = Git Author commits, project[4] = Git Tag files,
#   project[5] = Mock imports in Test files, project[6] = Mock frameworks in Test files

nonProjectFiles = ['AllMetrics.csv','AllMockImports.csv','AllMockFrameworks.csv']
# Set files that CSVReader should ignore here.

metricHeaders = []
# Used by 'AllMetrics' to search for specific metrics listed in UndProject CSV files.

def CSVMetrics(projectName):
    ''' Reads CSV metrics of a java project. Returns info gathered from UND metrics in
            a 2D array consisting of projectFiles and testFiles.
        projectName(string): Name of Java project.
    
    '''
    reader = open('UndProjects/' + projectName + '.csv')
    csvInput = csv.reader(reader)
    global metricHeaders
    if metricHeaders == []:
        metricHeaders = next(csvInput)

    projectFiles = []
    testFiles = []
    for row in csvInput:
        if row[0] == "File":
            projectFiles.append(row)
            if 'test' in row[1].lower():
                testFiles.append(row)
    
    reader.close()
    return [projectFiles,testFiles]

def GitCommitMetrics(projectName, validLines):
    ''' Reads Git metrics of a java project. Returns info about commits. 
        projectName (string): Name of Java project.
        validLines ([string]): List of 'keywords' searched for in the commits.
    '''
    reader = open('UndProjects/' + projectName + '.log', encoding='utf-8')
    authors = {}
    for line in reader:
        if line[0:3] in validLines:
            author = line[8:line.index('<')-1]
            if author not in authors:
                authors[author] = 0
            authors[author] += 1
        
    reader.close()
    authors = dict(sorted(authors.items(),key=lambda item: item[1],reverse=True))

    return authors

def GitReleaseMetrics(projectName):
    ''' Reads Git metrics of a java project. Returns info about releases. 
        projectName (string): Name of Java project.
    '''
    reader = open('UndProjects/' + projectName + '2.log', encoding='utf-8')
    validLines = ['tag:']
    tags = []
    for line in reader:
        valid = False
        for validLine in validLines:
            if validLine in line:
                valid = True
        if valid:
            tags.append(line)
        
    reader.close()
    return tags

def TestFileMetrics(projectName, testFiles):
    ''' Reads metrics of a java project's test files. Returns info about mock imports
            in a 2D array consisting of mockImports and mockFrameworks.
        projectName (string): Name of Java project.    
        testFiles (string): List of test files obtained from CSVMetrics.
    '''
    
    #Sanity check for projects with no test files.
    if testFiles == []:
        return 0

    
    #For some reason certain Und projects don't start their file paths at the actual project folder.
    #The path extensions help navigate to the corresponding test folder if necessary and can be added to.
    pathExtensions = ['','\\src']
    path = 0
    validPath = False
    while validPath == False:
        try:
            reader = open(projectName + pathExtensions[path] + '\\' + testFiles[0][1],encoding='utf-8')
        except FileNotFoundError:
            if path < len(pathExtensions)-1:
                path+=1
            else:
                raise FileNotFoundError(projectName + '\\' + testFiles[0][1])
        else:
            reader.close()
            validPath = True
            
    mockImports = {}
    # MockImports is a dict where the key is filePath, and the value is 
    # a list of validMock lines in the file.

    mockFrameworks = {}
    # mockFrameworks is a dict where the key is the framework, and the value
    # is a list of files where the framework is called.
    
    for file in testFiles:
        filePath = projectName + pathExtensions[path] + '\\' + file[1]
        reader = open(filePath,encoding='utf-8')
        fileName = file[1][(file[1].rindex('\\') + 1):-5]
        fileFrameworks = []
        
        for line in reader:
            if line.strip()[0:6] == "import" and 'mock' in line.lower() and "apache" not in line.lower():
#debug                print(projectName + "," + fileName + ": " + line)
                framework = IdentifyMockFramework(line)
                
                if filePath not in mockImports:
                    mockImports[filePath]=[]    
                mockImports[filePath].append([line.strip()[7:-1],framework])
                

                if framework not in fileFrameworks:
                    fileFrameworks.append(framework)
            if 'class' in line and fileName in line:
                break
        reader.close()
        
        for framework in fileFrameworks:
            if framework not in mockFrameworks:
                mockFrameworks[framework]=[]
            mockFrameworks[framework].append(filePath)
            
    return [mockImports,mockFrameworks]
        
def IdentifyMockFramework(importLine):
    ''' Trims/reads a line that is importing an outside mocking 
        framework to identify the framework used.
        importLine (string): Raw line of code that imports a mocking tool.
    '''
    importLine = importLine.strip().lower()
    
    #Check/Handling for if 'import' still remains in import line.
    if importLine[0:6] == 'import':
        importLine = importLine[7:]
    
    #Check/Handling for Static, which does not follow the "xxx.xxxx.xxxx." format.
    if importLine[0:6].lower() == 'static':
        importLine = importLine[7:]
    
    #If a 'xxx.' section matches one of these, it is a prefix to the framework import and can be removed.
    nonFrameworkPrefixes = ['okhttp3','org','io','com','compile','google','api','net','jvnet','javacrumbs']
    #Important question to ask later: What is 'compile.scomp.common.mockobj'? Notably used in Xmlbeans for something related 
    #to mock objects but can't find any online frameworks discussing scomp and mocking.
    
    validLine = False
    while not validLine:
        validLine = True
        nextPeriod = importLine.index(".")
        
        #Check/Handling for Github imports, which include the name of the repo owner afterwards.
        if importLine[:nextPeriod] == "github":
            importLine = importLine[nextPeriod + 1:]
            nextPeriod = importLine.index(".")
            importLine = importLine[nextPeriod + 1:]
            validLine = False

        if importLine[:nextPeriod] in nonFrameworkPrefixes:
            importLine = importLine[nextPeriod + 1:]
            validLine = False

    return importLine[:nextPeriod]
            
            
        
for file in os.listdir('UndProjects'):
    if '.csv' in file and file not in nonProjectFiles:
        Projects.append([file[:-4]])


print('Reading Projects...')

for project in Projects:
    print('   Reading ' + project[0] + '...')
    #Read CSV metrics
    Files = CSVMetrics(project[0])
    project.append(Files[0])
    project.append(Files[1])
    
    #Read Git commit log metrics
    project.append(GitCommitMetrics(project[0], ['Aut']))
    
    #Read Git release/tag log metrics
    project.append(GitReleaseMetrics(project[0]))
      
    #Read Test file import metrics
    TestMetrics = TestFileMetrics(project[0], project[2])
    project.append(TestMetrics[0])
    project.append(TestMetrics[1])


#    print("\nProject: " + project[0])
#    print("Number of .java files: " + str(len(project[1])))
#    print("Number of test files: " + str(len(project[2])))

print('Writing to "UndProjects/AllMetrics.csv"...')
reader = open('UndProjects/AllMetrics.csv','w', newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name', 'commits', 'contributors', 'releases', '.java Files', 'LOC',
                        'test Files', 'test LOC','\'Mock\' imports', 'Mocking frameworks'])

LOCColumn = metricHeaders.index('CountLineCode')

for project in Projects:
    name = project[0]
    totalFiles = len(project[1])
    totalTestFiles = len(project[2])
    LOC = 0
    for file in project[1]:
        LOC += int(file[LOCColumn])
    testLOC = 0
    for file in project[2]:
        testLOC += int(file[LOCColumn])
    commits = sum(project[3].values())
    contributors = len((project[3].keys()))
    releases = len((project[4]))
    mockedTests = len(project[5])
    mockingFrameworks = len(project[6])
    csvOutput.writerow([name, commits, contributors, releases, totalFiles, LOC, totalTestFiles, testLOC, mockedTests, mockingFrameworks])

reader.close()

print('Writing to "UndProjects/AllMockImports.csv"...')
reader = open('UndProjects/AllMockImports.csv','w', newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name', 'File Path', 'Mocking Framework', 'Imported Mock Line'])

for project in Projects:
    name = project[0]
    for file in project[5].keys():
        filePath = file
        for mockImport in project[5][filePath]:
            importLine = mockImport[0]
            framework = mockImport[1]
            csvOutput.writerow([name,filePath,framework,importLine])
            
reader.close()

print('Writing to "UndProjects/AllMockFrameworks.csv"...')
reader = open('UndProjects/AllMockFrameworks.csv','w', newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name','Mocking Framework','Used in # of Files'])

for project in Projects:
    name = project[0]
    for framework in project[6].keys():
        numUses = len(project[6][framework])
        csvOutput.writerow([name,framework,numUses])

reader.close()
print ('-- CSVReader.py Complete --')


