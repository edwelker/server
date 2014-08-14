#!/bin/env python
from __future__ import print_function
from kazoo.client import KazooClient

def connect():
	zk = KazooClient(hosts='127.0.0.1:2181', read_only=True)
	zk.start()
	return zk

def getdata(zk):
	("The node /edserver exists: {}".format(zk.exists('/edserver')))

	# data, stat = zk.get('/edserver')
	children = zk.get_children('/edserver')
	print("These are the children: {}\n".format(children))

	for child in children:
		print("For child: {}---------------".format(child))	
		data, stat = zk.get("/edserver/{}".format(child))
		if data == None:
		  data = "None found"
		print("Version: {}, data: {}".format(stat.version, data.decode("utf-8")))

if __name__ == "__main__":
	try:
		zk = connect()
		print("Connected to Zookeeper")
	except:
		print("Caught unexpected exception")
	getdata(zk)
