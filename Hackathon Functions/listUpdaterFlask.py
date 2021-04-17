#to start server, do:
#$ export FLASK_APP=posting.py
#$ flask run
from flask import Flask, request, jsonify

app = Flask(__name__)

numValues=5
dataFormat={0:'title',1:'description', 2:'sampleInput', 3:'sampleOutput', 4:'explanation'}

numData=0
myList=[] #list with data (dicts with elements: title, description, sampleInput, sampleOutput, explanation - all strings)

dataFile='questiondata.txt'

def read_data(filename):
    global myList, numData
    f=open(filename, 'r')
    checker=f.readline()
    if(checker==''):
        return
    numData=int(checker)
    for i in range(0, numData):
        entry={}
        data=f.readline().rstrip('\n').split(',')
        for j in range(0, numValues):
            entry[dataFormat[j]]=data[j]
        myList.append(entry)
    f.close()

def store_data(filename):
    global myList, numData
    f=open(filename, 'w')
    f.write(str(numData)+'\n')
    for entry in myList:
        first=0
        for j in range(0, numValues):
            if(first!=0):
                f.write(',')
            first=1
            f.write(entry[dataFormat[j]])
        f.write('\n')
    f.close()

#startup
read_data(dataFile)

def save_data(file):
    global numData
    #check if same title is in data list
    for index in range(0, numData):
        entry=myList[index]
        #print(file['title'])
        if(file['title']==entry['title']):
            #same title found, overwrite data
            for dataTypeIndex in range(0, numValues):
                entry[dataFormat[dataTypeIndex]]=file[dataFormat[dataTypeIndex]]
            return 'successOverwrite'

    #otherwise, create copy of data entry
    entry={}
    for dataTypeIndex in range(0, numValues):
        entry[dataFormat[dataTypeIndex]]=file[dataFormat[dataTypeIndex]]
    myList.append(entry)
    numData+=1
    return 'successAdd'

def print_entry(entry):
    for dataTypeIndex in range(0, numValues):
        print(entry[dataFormat[dataTypeIndex]])

@app.route("/save_data", methods=["POST"]) #adds/overwrites entry to myList
def get_data():
    file = request.form.to_dict()
    print(file['title'])
    #print_entry(file)
    print('data received')
    status=save_data(file)
    store_data(dataFile)
    print('data saved')
    print(myList)
    return jsonify({'status': status})

@app.route("/get_data", methods=["POST"])
def retrieve_data():
    file = request.form.to_dict()
    for entry in myList:
        if(file['title']==entry['title']):
            return jsonify({'status': 'found', 'data':entry})
    return jsonify({'status':'not found'})    

if __name__ == "__main__":
    app.run(debug=True)