from flask import Flask, request, jsonify
from reTelling import *


app = Flask(__name__)

@app.route('/retelling', methods=['POST'])
def retelling():
    data = request.json
    text = data.get('text')
    if not text:
        return jsonify({'error': 'No text provided'}), 400

    try:
        result = process_large_text(text)
        return jsonify({'result': result})
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)