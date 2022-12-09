from flask import Flask ,request  # 서버 구현을 위한 Flask 객체 import
import sys, os
from aws_transcribe import transcribe
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
app = Flask(__name__)  # Flask 객체 선언

@app.route('/analysis',methods=['POST'])
def analysis():
        video_title=request.form['title'] #동영상 제목 추출
        result=transcribe(video_title)
        return result

if __name__=='__main__':
     app.run(host='0.0.0.0', port=5000, debug=True)
