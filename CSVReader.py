import os
import csv

''' Python file intended to be run in a batch script to read and write metrics 
    from CSV files generated from Understand Scitool projects.
'''

print('-- CSVReader.py loaded --')

Projects = []
#2D Array, project[0] = Project name, Project[1] = All files, Project[2] = Test files


for file in os.listdir('UndProjects'):
    if '.csv' in file and file != 'AllMetrics.csv':
        Projects.append([file])
#print(Projects)
        
print('Reading Projects...')
for project in Projects:
    reader = open('UndProjects/' + project[0])
    csvInput = csv.reader(reader)

    ProjectFiles = []
    TestFiles = []
    for row in csvInput:
        if row[0] == "File":
            ProjectFiles.append(row)
            if 'test' in row[1].lower():
                TestFiles.append(row)
    
    reader.close()
    
    project.append(ProjectFiles)
    project.append(TestFiles)
        
#    print("\nProject: " + project[0])
#    print("Number of .java files: " + str(len(project[1])))
#    print("Number of test files: " + str(len(project[2])))

print('Writing to "UndProjects/AllMetrics.csv"...')
reader = open('UndProjects/AllMetrics.csv','w', newline = '')

csvOutput = csv.writer(reader)
csvOutput.writerow(['Project Name', '.java Files', 'LOC', 'test Files', 'test LOC'])

for project in Projects:
    name = project[0][:-4]
    totalFiles = len(project[1])
    totalTestFiles = len(project[2])
    LOC = 0
    for file in project[1]:
        LOC += int(file[2])
    testLOC = 0
    for file in project[2]:
        testLOC += int(file[2])
    
    csvOutput.writerow([name, totalFiles, LOC, totalTestFiles, testLOC])

reader.close()

print ('-- CSVReader.py Complete --')