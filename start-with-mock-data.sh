docker run -p 8080:8080 -d --rm --name reboarding-test reboarding --date.stepTo10=$(date '+%Y-%m-%d') --date.stepTo20=$(date -d '+1 day' '+%Y-%m-%d') --date.stepTo30=$(date -d '+2 day' '+%Y-%m-%d') --date.stepTo50=$(date -d '+3 day' '+%Y-%m-%d') --date.stepTo100=$(date -d '+4 day' '+%Y-%m-%d')