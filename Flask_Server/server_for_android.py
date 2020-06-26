# 导入模块
import json
import os

from flask import Flask, jsonify, Response
from flask import request
import pymysql
from werkzeug.utils import secure_filename

import config
import uuid
import datetime
from smtplib import SMTP_SSL
from email.header import Header
from email.mime.text import MIMEText
# from PIL import Image

# 创建flask对象
app = Flask(__name__)
conn = pymysql.connect(host="192.168.192.128", port=3306, user="root", password="hzc", database="test", charset="utf8")
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'JPG', 'PNG', 'bmp'])
mailInfo = {
    "from": "1763215059@qq.com",
    "to": "962073795@qq.com",
    "hostname": "smtp.qq.com",
    "username": "1763215059@qq.com",
    "password": "orxmtxjbdwbdeaed",
    "mailsubject": "验证码",
    "mailtext": "hello, this is send mail test.",
    "mailencoding": "utf-8"
}


@app.route('/')
def test():
    return '服务器正常运行'


@app.route('/email_captcha', methods=['POST', 'GET'])
def email_captcha():
    email = request.form['email']
    print(email)
    if not email:
        return config.GET_EMAIL_FORM_ERRO  # restful. 封装的函数，返回前端数据

    '''
        生成随机验证码，保存到memcache中，然后发送验证码，与用户提交的验证码对比
        '''
    captcha = str(uuid.uuid1())[:6]  # 随机生成6位验证码

    text = "您的验证码是：" + captcha

    smtp = SMTP_SSL(host=mailInfo["hostname"], port=465)
    smtp.set_debuglevel(1)
    smtp.ehlo(mailInfo["hostname"])
    smtp.login(mailInfo["username"], mailInfo["password"])
    msg = MIMEText(text, _charset=mailInfo['mailencoding'])
    msg["Subject"] = Header(mailInfo["mailsubject"], mailInfo['mailencoding'])
    msg["from"] = "JasonHu for test"
    # 给用户提交的邮箱发送邮件
    try:
        # print(msg)
        smtp.sendmail(mailInfo["from"], email, msg.as_string())
        smtp.quit()
    except:
        return config.GET_EMAIL_ERRO
    return captcha


# 此方法处理用户登录 返回码为0无注册 返回码为1密码错误
@app.route('/user', methods=['POST', 'GET'])
def check_user():
    """
    处理登录
    :return:
    """
    username = request.form['userName']
    pass_word = request.form['password']
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "select * from userinfo where userName = '%s';" % str(username).strip()
    try:
        cursor.execute(sql)
    except:
        return config.LOGIN_ERRO
    users = cursor.fetchall()
    cursor.close()
    print(users)
    if len(users) == 0:
        return config.LOGIN_ERRO
    else:
        for item in users:
            if str(pass_word).strip() == str(item['password']).strip():
                return config.LOGIN_OK
            else:
                return config.PASSWORD_ERRO


# 此方法处理用户注册
@app.route('/register', methods=['POST', 'GET'])
def register():
    username = request.form['userName']
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "select * from userinfo where userName = '%s';" % str(username).strip()
    try:
        cursor.execute(sql)
    except:
        return config.LOGIN_ERRO
    users = cursor.fetchall()
    if len(users) == 0:
        password = str(request.form['password']).strip()
        email = str(request.form['email']).strip()
        city = str(request.form['City']).strip()
        phoneNumber = str(request.form['phoneNumber']).strip()
        LogState = str(request.form['LogState']).strip()
        try:
            image_head = str(request.form['image_head'])
        except:
            image_head = ''
        print(username, password, email, phoneNumber, LogState, city, image_head)
        sql = "insert into userinfo(userName, password, email, phoneNumber, LogState, City, image_head) " \
              "values('%s','%s','%s','%s','%s','%s','%s')" \
              % (username, password, email, phoneNumber, LogState, city, image_head)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        return config.REGISTER_OK
    else:
        return config.REGISTER_ERRO


@app.route('/get_message_from_', methods=['GET', 'POST'])
def get_message_from_():
    message = request.form['Msg']
    from_user_name = request.form['current_user']
    to_user = request.form['to_user']
    date = request.form['current_date']
    print(date)
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "insert into message(message_data, to_user, from_user_name) " \
          "values('%s','%s','%s')" \
          % (message, to_user, from_user_name)
    cursor.execute(sql)
    conn.commit()
    cursor.close()
    return "ok"


@app.route('/get_user_info', methods=['GET', 'POST'])
def get_user_info():
    username = request.form['userName']
    pass_word = request.form['password']
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "select * from userinfo where userName = '%s';" % str(username).strip()
    # 查询用户信息
    try:
        cursor.execute(sql)
    except:
        return config.NO_USER_ERRO
    users = cursor.fetchall()
    cursor.close()
    print(users)
    if len(users) == 0:
        return config.NO_USER_ERRO
    else:
        for item in users:
            if str(pass_word).strip() == str(item['password']).strip():
                return json.dumps(item, ensure_ascii=False)
            else:
                return config.PASSWORD_ERRO


# def thumbnail_pic(path):
#     """
#     生成略缩图
#     :param path: 路径
#     :return:
#     """
#     # glob.glob(pathname)，返回所有匹配的文件路径列表
#     im = Image.open(path)
#     im.thumbnail((80, 80))
#     print(im.format, im.size, im.mode)
#     im.save(path + "little.jpg", 'JPEG')
#     print('Done!')
#     return path + "little.jpg"


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/getPicFromAndroid/', methods=['POST', 'GET'])
def upload():
    if request.method == 'POST':
        # 通过file标签获取文件
        userName = request.form['userName']
        # file1 = request.form['file'] 报错 file只能用request.files['file']
        f = request.files['file']
        if f and allowed_file(f.filename):
            base_path = 'C:/users/JasonHu/PycharmProjects/untitled/picture/' + \
                        userName + '/'
            if not os.path.exists(base_path):
                os.makedirs(base_path)
            upload_path = base_path + secure_filename(f.filename)
            # 保存文件
            f.save(upload_path)
            cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
            sql = "update userinfo set image_head = '%s' where userName = '%s';" % (str(upload_path).strip(), str(
                userName).strip())
            cursor.execute(sql)
            cursor.close()
            conn.commit()
            # 返回上传成功界面
            return "上传成功"
        else:
            return jsonify({"error": 1001, "msg": "图片类型：png、PNG、jpg、JPG、bmp"})
    else:
        return "上传失败"


@app.route('/putPictoAndroid', methods=['POST', 'GET'])
def get_head_pic():
    username = request.form['userName']
    pic_name = request.form['pic_name']
    quality = request.form['pic_quality']
    image_addr = 'C:/users/JasonHu/PycharmProjects/untitled/picture/' + \
                 username + '/' + pic_name
    if quality == "原图":
        image = open(image_addr, 'rb').read()
    else:
        if os.path.exists(image_addr + "little.jpg"):
            image = open(image_addr + "little.jpg", 'rb').read()
        else:
            # new_image_addr = thumbnail_pic(image_addr)
            image = open(image_addr, 'rb').read()
    resp = Response(image, mimetype="image/jpeg")
    return resp


@app.route('/?method=isOnline', methods=['POST', 'GET'])
def isOnline():
    username = request.form['userName']
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "select * from userinfo where userName = '%s';" % str(username).strip()
    nowTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')  # 现在
    # todo 提醒服务器用户username上线了
    print(username, nowTime)
    return "1"
    pass


@app.route('/get_friend_list', methods=['POST', 'GET'])
def get_friend_list():
    username = request.form['userName']
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "select * from friendlist where user = '%s';" % str(username).strip()
    # 查询用户信息
    try:
        cursor.execute(sql)
    except:
        return config.NO_USER_ERRO
    users = cursor.fetchall()
    print(users)
    if len(users) == 0:
        return config.NO_USER_ERRO
    else:
        return json.dumps({"friendList": users}, ensure_ascii=False)


@app.route('/update_userInfo', methods=['POST', 'GET'])
def update_userInfo():
    type_ = request.form["type"]
    username = request.form["userName"]
    data = request.form[type_]
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)  # 使用字典格式输出
    sql = "update userinfo SET %s = %s where userName = '%s';" % (str(type_).strip(), str(data).strip(),
                                                                  str(username).strip())
    try:
        cursor.execute(sql)
    except:
        return ""
    conn.commit()
    cursor.close()
    return "ok"


if __name__ == '__main__':
    app.run('127.0.0.1', 8080, debug=True)
