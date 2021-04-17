#to start server, do:
#$ export FLASK_APP=posting.py
#$ flask run
from flask import Flask, request, jsonify
from PIL import Image

app = Flask(__name__)

@app.route("/get_confirm", methods=["POST"])
def get_confirm():
    return jsonify({'status': 'success'})

@app.route("/get_fail", methods=["POST"])
def get_fail():
    return jsonify({'status': 'failed'})

if __name__ == "__main__":
    app.run(debug=True)