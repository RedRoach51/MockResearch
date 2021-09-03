

import csv

text = []

methods = []
origins = []
counts = []

result = []

method_added = False


f = open("any23.txt")

for line in f:
    text.append(line)



for i, line in enumerate(text):
    method_added = False

    if(line[1:7] == 'Method'):

        for j, method in enumerate(methods):
            if (method == line[14:]):
                counts[j] += int(text[i+2][8])
                method_added = True
        
                break

        if(method_added == False):
            methods.append(line[14:])
            origins.append(text[i+1][15:])
            counts.append(int(text[i+2][8]))



result.append("any23")

for i in range(0,len(methods)):
    result.append(methods[i])
    result.append(origins[i])
    result.append(counts[i])


for item in result:
    print(item)


with open("API analysis.csv",'w') as CSVFile:

    CSVWriter = csv.writer(CSVFile)

    CSVWriter.writerow(['project name', 'API name', 'API origin', 'API count'])

    CSVWriter.writerow(result)
            
            
        # print(line[14:])



print(text[18] == text[36])

# print(len(text))

# print(text[2][1])
