import os

fileArray = []
folderArray = []

def folderSearch(space, prevPath, testFolder):
    ''' Opens a folder prevPath (String) and finds all files inside. Will also open subfolders.
        If folderSearch is run without a folder path, it will start from FileSearcher directory.
        
        Currently coded to find and save all '.java' test files to fileArray.
        Assumes 'test' folders contain test files.
        
        (int) Space, (String/None) prevPath, (Boolean) testFolder
    '''
    
#debug
#    if prevPath != None:
#        print("*"+prevPath)

    for file in os.listdir(prevPath):
            
        if prevPath == None:
            filePath = file;
            
        else:
            filePath = prevPath + "/" + file
        
        if(os.path.isdir(filePath)):
            folderArray.append(filePath)
        
            # 'Could use this to help identify test files.
            if "test" in file.lower():
                testFolder = True
                
            
            folderSearch(space + 3, filePath, testFolder)

        else:
            # 'Used to identify .java test files.'
            #if (file[len(file)-5:] == ".java" and "test" in file.lower()): \
#debug               or (file[len(file)-5:] == ".java" and testFolder):
            #       print(filePath)
            #       fileArray.append(filePath)
                   
            # 'Used to just identify all .java files'
            if (file[len(file)-5:] == ".java"):
                print(filePath)
                fileArray.append(filePath)

def testReader(filePath):
    ''' Reads a file

        (String) filePath
    '''
    try:
        reader = open(filePath)
        output = open("output.txt", "a")
        for line in reader:
            output.write(line)
        reader.close()
        output.close()
    except FileNotFoundError:
        print("(Error) File not found at: " + filePath)
    
        
    

folderSearch(0,"../axiom",False)
print("Number of files found: " + str(len(fileArray)))
print("Number of folders found: " + str(len(folderArray)))

testReader(fileArray[-1])



