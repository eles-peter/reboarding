docker run \
  -p 8080:8080 \
  -d --rm \
  --name reboarding-test reboarding \
  --date.stepTo5="$(date '+%Y-%m-%d')" \
  --date.stepTo4="$(date -d '+1 day' '+%Y-%m-%d')" \
  --date.stepTo3="$(date -d '+2 day' '+%Y-%m-%d')" \
  --date.stepTo2="$(date -d '+3 day' '+%Y-%m-%d')" \
  --date.stepTo1="$(date -d '+4 day' '+%Y-%m-%d')" \
  --date.endOfPeriod="$(date -d '+5 day' '+%Y-%m-%d')"
