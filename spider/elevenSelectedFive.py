
# -*- coding: utf-8 -*-

import urllib2
import urllib
import re
import string
import MySQLdb
import time
import datetime
import random
from TaskTimer import TaskTimer
from agency.agency_tools import proxy

class Ticai:
    def __init__(self):
        self.number = [] 

    def ticai_crawl(self,url,data):
        #proxy_info = {'host': '119.101.114.196',
        #          'port': 9999
        #}
        #proxy_support = urllib2.ProxyHandler({"https": "https://%(host)s:%(port)d" % proxy_info})
        #print self._proxies
        #proxy_support = urllib2.ProxyHandler(self._proxies)
        #opener = urllib2.build_opener(proxy_support)
        #urllib2.install_opener(opener)
        print url
        Max_Num=6
        response=''
        for i in range(Max_Num):
            try:
                req = urllib2.Request(url, data)
                response = urllib2.urlopen(req,timeout=5)
                break
            except:
                print "url open exception"
                if i < Max_Num:
                    continue
                else :
                    print 'URLError: <urlopen error timed out> All times is failed '
                    return
        self.deal_data(response.read())

    def deal_data(self,response): 
        myPage=response.replace('\"}','<tr class=\'bg\'>')
        myqiItems =  re.findall('<tr><td>(.*?)</td><tr class=\'bg\'>',myPage,re.S)
        myqiItems +=  re.findall('<tr class=\'bg\'><td>(.*?)</td><tr>',myPage,re.S)
        db = MySQLdb.connect("localhost", "root", "3664", "test", charset='utf8' )
        
        for qiItems in myqiItems:
            print qiItems
            period = qiItems[0:9].replace("-","")
            print period
            time = re.findall('<td class=\'tdright\'>(.*?)</td>',qiItems,re.S)
            num =  qiItems[-14:]
            print num
            print time
            cursor = db.cursor()
            sql = "INSERT INTO lottery(id, \
                    time, numbers) \
                    VALUES ('%d', '%s', '%s')" % \
                    (int(period), time[0], num)
            try:
                cursor.execute(sql)
                db.commit()
            except:
                db.rollback() 
                print "rollback"
        db.close()

    def gen_dates(self,start, days):
        day = datetime.timedelta(days=1)
        for i in range(days):
            yield start + day*i


    def get_date_list(self,start=None, end=None):
        if start is None:
            start = datetime.datetime.strptime("2019-10-20", "%Y-%m-%d")
        if end is None:
            end = datetime.datetime.now()
        data = []
        if start==end:
            data.append(end)
            return data
        for d in self.gen_dates(start, (end-start).days):
            data.append(d)
        return data
    def crawl(self,start=None, end=None):
        url =r'https://kaijiang.aicai.com/open/kcResultByDate.do'
        values = {"gameIndex" : "303","searchDate" : "2019-10-11"}
        for i in self.get_date_list(start,end):
            dayname =i.strftime("%Y-%m-%d")
            print dayname
            values["searchDate"]=dayname
            data = urllib.urlencode(values)
            self.ticai_crawl(url,data)
            time.sleep(random.random())
        #self.db.close()


if __name__ == '__main__':
    ticai = Ticai()
    #ticai.crawl(datetime.datetime.now()-datetime.timedelta(days =1),datetime.datetime.now())
    #ticai.crawl(datetime.datetime.strptime("2009-11-01", "%Y-%m-%d"),datetime.datetime.strptime("2012-07-02", "%Y-%m-%d"))
    timer = TaskTimer()
    timer.join_task(ticai.crawl, [datetime.datetime.now()-datetime.timedelta(days =1),datetime.datetime.now()], timing=3)
    timer.start()

'''
create table lottery (
  id varchar(8)  primary key,
  time varchar(16),
  numbers varchar(14) not null
);
'''
