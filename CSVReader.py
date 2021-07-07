import os
import csv

''' Python file intended to be run in a batch script to read and write metrics 
    from CSV files generated from Understand Scitool projects.
'''

print('-- CSVReader.py loaded --')

Projects = []
#2D Array of project[] objects, project[0] = Project name, project[1] = All files, 
#   project[2] = Test files, project[3] = Git Author commits, project[4] = Git Tag files
#   project[5] = Git 


metricHeaders = []
# Used by 'AllMetrics' to search for specific metrics listed in UndProject CSV files.

def CSVMetrics(projectName):
    ''' Reads CSV metrics of a java project. Returns info gathered from UND metrics in
            a 2d array consisting of projectFiles and testFiles.
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
    ''' Reads metrics of a java project's test files. Returns info about mock imports.
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
            validPath = True
            
        
    numMock = 0        
    for file in testFiles:
        reader = open(projectName + pathExtensions[path] + '\\' + file[1],encoding='utf-8')
        fileName = file[1][(file[1].rindex('\\') + 1):-5]
        for line in reader:
            if line[0:6] == "import" and 'mock' in line.lower():
                numMock+= 1
#debug                print(projectName + "," + fileName + ": " + line)
                break
            if 'class' in line and fileName in line:
                break
        reader.close()
    return numMock
        
    

for file in os.listdir('UndProjects'):
    if '.csv' in file and file != 'AllMetrics.csv':
        Projects.append([file[:-4]])


print('Reading Projects...')
for project in Projects:
    
    #Read CSV metrics
    Files = CSVMetrics(project[0])
    project.append(Files[0])
    project.append(Files[1])
    
    #Read Git commit log metrics
    project.append(GitCommitMetrics(project[0], ['Aut']))
    
    #Read Git release/tag log metrics
    project.append(GitReleaseMetrics(project[0]))
      
    #Read Test file import metrics
    project.append(TestFileMetrics(project[0], project[2]))
    
#    print("\nProject: " + project[0])
#    print("Number of .java files: " + str(len(project[1])))
#    print("Number of test files: " + str(len(project[2])))

print('Writing to "UndProjects/AllMetrics.csv"...')
reader = open('UndProjects/AllMetrics.csv','w', newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name', 'commits', 'contributors', 'releases', '.java Files', 'LOC', 'test Files', 'test LOC','\'Mock\' imports'])

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
    mockImports = project[5]
    csvOutput.writerow([name, commits, contributors, releases, totalFiles, LOC, totalTestFiles, testLOC, project[5]])

reader.close()

print ('-- CSVReader.py Complete --')

