import requests

url_confirm = 'http://127.0.0.1:5000/get_confirm'
url_fail= 'http://127.0.0.1:5000/get_fail'

r = requests.post(url_confirm)
s=requests.post(url_fail)


# convert server response into JSON format.
print(r.json())
print(s.json())