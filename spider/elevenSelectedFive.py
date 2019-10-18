
# -*- coding: utf-8 -*-

import urllib2
import re
import string
import MySQLdb
import time
from agency.agency_tools import proxy

class Ticai:
    def __init__(self):
        self.number = []
        self.proxy = proxy()
        self._proxies = self.proxy.setProxy()

    def ticai_crawl(self,url):
        Max_Num=4
        for i in range(Max_Num):
            proxy_support = urllib2.ProxyHandler({"https": self._proxies})
            opener = urllib2.build_opener(proxy_support)
            urllib2.install_opener(opener)
            try:
                response=urllib2.urlopen(url,timeout=5)
                self.deal_data(response.read())
                break
            except:
                self._proxies = self.proxy.setProxy()
                if i < Max_Num:
                    continue
                else :
                    print 'URLError: <urlopen error timed out> All times is failed '

    def deal_data(self,myPage): 
	myqiItems =  re.findall('<div.*?class="pig-uul-ll">(.*?)</div>\s</div>\s</div>',myPage,re.S)
        for qiItems in myqiItems:
            qiItems=qiItems+r"</div></div>"
            qishus = re.findall('<span>(.*?)</span>',qiItems,re.S)
            #print 'here'
            numlist = []
            period = ''
            time = ''
            for qishu in qishus:
                if len(qishu) == 8: 
                    period=qishu
                elif len(qishu) == 10: 
                    time=qishu
                elif len(qishu) == 2: 
                    numlist.append(qishu)
                else:
                    break 
            if len(numlist)==5 and qishu!='' and period!='': 
                numlist.sort()
                print ','.join(numlist)

                cursor = db.cursor()
                sql = "INSERT INTO lottery(id, \
                    time, numbers) \
                    VALUES ('%d', '%s', '%s')" % \
                    (int(period), time, ','.join(numlist))
                try:
                    cursor.execute(sql)
                    db.commit()
                except:
                    db.rollback()
                    print "rollback"

if __name__ == '__main__':

    db = MySQLdb.connect("localhost", "root", "3664", "test", charset='utf8' )

    ticai = Ticai()

    url =r'http://wap.sdticai.com/index.php?g=Portal&m=Index&a=lottery_history&id=9&pageindex='
    for i in range(0,20): #(2599,3000): 
        ticai.ticai_crawl(url+str(i))
        time.sleep(0.05)
    db.close()
'''
create table lottery (
  id varchar(8)  primary key,
  time varchar(10),
  numbers varchar(14) not null
);
'''

