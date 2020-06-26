from PIL import Image


def thumbnail_pic(path):
    # glob.glob(pathname)，返回所有匹配的文件路径列表
    im = Image.open(path)
    im.thumbnail((80, 80))
    print(im.format, im.size, im.mode)
    im.save(path+"little.jpg", 'JPEG')
    print('Done!')


image_addr = 'C:/users/JasonHu/PycharmProjects/untitled/picture/' + \
                 "大哥9" + '/' + "2019-12-03-14-45-06.jpg"
thumbnail_pic(image_addr)