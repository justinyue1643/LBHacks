from flask import Flask, request, jsonify
import requests

url='http://127.0.0.1:5000/save_data'
urlget='http://127.0.0.1:5000/get_data'

data={'title':'hello1', 'description':'thisssdafdsfd', 'sampleInput':'is', 'sampleOutput':'a', 'explanation':'test'}
r=requests.post(url, data)
print(r.json())

#s=requests.post(urlget, {'title':'hello'})
#print(s.json())