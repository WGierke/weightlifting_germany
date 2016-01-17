while true
do
	date +"%T"
	git fetch --all
	git reset --hard origin/master
	python server/json_parser.py
	CHANGES=$(git status | grep modified: | awk '{split($0,a,"/"); print a[2]}' | sed 's/\.json//' | tr '\n' ' ')
	git add --all
	git commit -m "Update: $CHANGES"
	git push
	python server/push.py
	sleep 1800 #30 minutes
done
