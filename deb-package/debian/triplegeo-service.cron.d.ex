#
# Regular cron jobs for the triplegeo-service package
#
0 4	* * *	root	[ -x /usr/bin/triplegeo-service_maintenance ] && /usr/bin/triplegeo-service-ui_maintenance
