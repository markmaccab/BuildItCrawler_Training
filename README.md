Github link is https://github.com/markmaccab/BuildItCrawler
To build and run the solution in any directory, do:
git clone https://github.com/markmaccab/BuildItCrawler
cd BuildItCrawler
mvnw spring-boot:run
BuildItCrawler is a microservice
Input is: http://localhost:8080/?ROOT_URL=http://wsj.com. This is example url
Output are 3 lists showing one after another (see sample below):
Image Links:
https://images.wsj.net/im-254077?width=110&height=73
...
Internal Links:
http://wsj.com
External Links:
http://bigcharts.marketwatch.com
...