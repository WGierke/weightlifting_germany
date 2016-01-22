#!/usr/bin/python
# -*- coding: iso-8859-15 -*-
from gcm import GCM
from collections import Counter
from tabulate import tabulate
import requests
import json
import ConfigParser
import os
import sys

def send_parse_api_request(method, url, app_id, rest_key):
    request_method = getattr(requests, method)
    response = request_method(url, headers={"Content-Type": "application/json", "X-Parse-Application-Id": app_id, "X-Parse-REST-API-Key": rest_key}).content
    return response

config = ConfigParser.RawConfigParser(allow_no_value=True)
config.read('server/config.ini')
application_id = config.get("parse", "X-Parse-Application-Id")
rest_key = config.get("parse", "X-Parse-REST-API-Key")

filter_responses = send_parse_api_request("get", "https://api.parse.com/1/classes/FilterSetting?limit=999", application_id, rest_key)
filter_objects = json.loads(filter_responses)["results"]
ordered_objects = list(reversed(filter_objects))

seen = []
filters = []
for obj in ordered_objects:
    user_id = obj["userId"]
    filter_setting = obj["filter"]
    if not user_id in seen:
        seen.append(user_id)
        filters.append(filter_setting)
    else:
        print "Sending request to remove outdated filter setting of user " + user_id
        send_parse_api_request("delete", "https://api.parse.com/1/classes/FilterSetting/" + obj["objectId"], application_id, rest_key)

print "Collected " + str(len(filters)) + " filter settings:"
filters = Counter(filters).most_common()
filters.insert(0, ('Filter', 'Count'))
print tabulate(filters)