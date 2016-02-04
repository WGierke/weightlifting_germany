#!/usr/bin/python
# -*- coding: iso-8859-15 -*-
import urllib2
import re
import os
import json
import codecs
from bs4 import BeautifulSoup


class BuliParser:

    def __init__(self, season, league, relay, competition_file_name, table_file_name):
        self.league = league
        self.relay = relay
        self.iat_season_base = "https://www.iat.uni-leipzig.de/datenbanken/blgew{0}/".format(season)
        self.iat_competitions_url = "{0}start.php?pid=%27123%27&resultate=1&bl={1}&staffel={2}".format(self.iat_season_base, league, relay)
        self.iat_table_url = "{0}start.php?pid=%27123%27&tabelle=1&bl={1}&staffel={2}".format(self.iat_season_base, league, relay)
        self.competition_file_name = "production/archive/" + season + "/" + competition_file_name + ".json"
        self.table_file_name = "production/archive/" + season + "/" + table_file_name + ".json"
        self.error_occured = False
        self.TIMEOUT = 15

    # Main functions
    def create_competitions_file(self):
        """Save past competitions in competition_file_name.json"""
        print "Parsing past competitions ..."
        try:
            print "Requesting " + self.iat_competitions_url
            competitions = urllib2.urlopen(self.iat_competitions_url, timeout=self.TIMEOUT).read().split("</TABLE>")[0]
        except Exception, e:
            print 'Error while downloading competitions ', e
            self.error_occured = True
            return

        re_competition_entry = re.compile(ur'(?<=class=font4>).*(?=[\r\n]?<\/TD>)')
        re_href = re.compile(ur'(?<=href=)[^>]*(?=>)')
        competition_entries = re.findall(re_competition_entry, competitions)
        competition_entries = [w.replace('\r', '').replace('<br>', ' ') for w in competition_entries]

        competitions_dict = {}
        final_competitions = []

        for i in range(0, len(competition_entries), 7):
            entry = {}
            entry["location"] = competition_entries[i+1]
            entry["date"] = competition_entries[i+2]
            entry["home"] = competition_entries[i+3]
            entry["guest"] = competition_entries[i+4]
            entry["score"] = competition_entries[i+5]
            entry["url"] = self.iat_season_base + re.findall(re_href, competition_entries[i+6])[0]

            final_competitions.append(entry)

        if len(final_competitions) == 0:
            return

        competitions_dict["past_competitions"] = final_competitions
        json_competitions = json.dumps(competitions_dict, encoding='latin1', sort_keys=True, indent=4, separators=(',', ': '))
        competitions_dict_json = "[" + json_competitions + "]"

        with open(self.competition_file_name, "w+") as f:
            f.write(competitions_dict_json.decode('utf-8'))
        return


    def create_table_file(self):
        """Save table entries in table_file_name.json"""
        print "Parsing table ..."
        try:
            table = urllib2.urlopen(self.iat_table_url, timeout=self.TIMEOUT).read().split("</TABLE>")[0]
        except Exception, e:
            print 'Error while downloading table ', e
            self.error_occured = True
            return

        re_table_entry = re.compile(ur'(?<=class=font4>).*(?=[\r\n]?<\/TD>)')
        table_entries = re.findall(re_table_entry, table)
        table_entries = [w.replace('\r', '') for w in table_entries]

        table_dict = {}
        final_entries = []

        for i in range(0, len(table_entries), 4):
            entry = {}
            entry["place"] = str(i/4+1)
            entry["club"] = table_entries[i]
            entry["score"] = table_entries[i+1]
            entry["max_score"] = table_entries[i+2]
            entry["cardinal_points"] = table_entries[i+3]
            final_entries.append(entry)

        if len(final_entries) == 0:
            return

        table_dict["table"] = final_entries
        json_table = json.dumps(table_dict, encoding='latin1', sort_keys=True, indent=4, separators=(',', ': '))
        table_dict_json = "[" + json_table + "]"

        with open(self.table_file_name, "w+") as f:
            f.write(table_dict_json.decode('utf-8'))
        return

    def create_buli_files(self):
        print "Creating Bundesliga files for BL " + self.league + " - " + self.relay
        for func in [self.create_competitions_file, self.create_table_file]:
            func()


if __name__ == '__main__':
    SEASON = "0708"
    BuliParser1South = BuliParser(SEASON, "1", "S%FCd", "1South_competitions", "1South_table")
    BuliParser1Center = BuliParser(SEASON, "1", "Mitte", "1Center_competitions", "1Center_table")
    BuliParser1North = BuliParser(SEASON, "1", "Nord", "1North_competitions", "1North_table")


    BuliParser2North = BuliParser(SEASON, "2", "Nord", "2North_competitions", "2North_table")
    BuliParser2NorthEast = BuliParser(SEASON, "2", "Nordost", "2Northeast_competitions", "2Northeast_table")
    BuliParser2NorthWest = BuliParser(SEASON, "2", "Nordwest", "2Northwest_competitions", "2Northwest_table")

    BuliParser2East = BuliParser(SEASON, "2", "Ost", "2East_competitions", "2East_table")

    BuliParser2South = BuliParser(SEASON, "2", "S%FCd", "2South_competitions", "2South_table")
    BuliParser2SouthEast = BuliParser(SEASON, "2", "S%FCdost", "2Southeast_competitions", "2Southeast_table")
    BuliParser2SouthWest = BuliParser(SEASON, "2", "S%FCdwest", "2Southwest_competitions", "2Southwest_table")

    BuliParser2West = BuliParser(SEASON, "2", "West", "2West_competitions", "2West_table")

    BuliParser2Middle = BuliParser(SEASON, "2", "Mitte", "2Center_competitions", "2Center_table")

    for parser in [BuliParser1South, BuliParser1Center, BuliParser1North, BuliParser2North, BuliParser2NorthEast, BuliParser2NorthWest, BuliParser2East, BuliParser2South, BuliParser2SouthEast, BuliParser2SouthWest, BuliParser2West, BuliParser2Middle, ]:
        parser.create_buli_files()
