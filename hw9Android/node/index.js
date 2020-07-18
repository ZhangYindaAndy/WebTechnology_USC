var express = require('express');
var request = require('request');
var router = express.Router();

router.get('/', (req, res) => {
    res.send("Yinda Zhang Server");
})

function formatDate(date_str)
{
    let date = new Date(date_str);
    
    let day = '' + date.getDate();
    let year = date.getFullYear();

    let month_list = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    let month = month_list[date.getMonth()];

    if(day.length < 2)
        day = '0' + day;

    return [day, month, year].join(' ');
}

function checkValid(data)
{
    if(data !== null && data !== undefined && data.length > 0)
        return true;
    return false;
}


//Guardian Search
// all share the same format
function parse_guardian_data(data)
{
    try{
        // console.log(data);
        let results;
        if(data["response"]["content"] === undefined)
            results = data["response"]["results"];   // search and section
        else
            results = [data["response"]["content"]];   // article

        let return_data = [];
        for(let i = 0; i < results.length; ++i)
        {
            let news = results[i];
            if(checkValid(news["sectionName"]) && checkValid(news["webTitle"]) && checkValid(news["webUrl"]) &&
                checkValid(news["id"]) && checkValid(news["webPublicationDate"]) &&
                checkValid(news["blocks"]["body"][0]["bodyHtml"]))
            {
                let img = "";
                if(news["blocks"]["main"] === undefined)
                    continue;
                let imgs = news["blocks"]["main"]["elements"][0]["assets"];

                if(imgs.length > 0 && imgs[imgs.length-1]["file"].length > 0)
                    img = imgs[imgs.length - 1]["file"];
                else
                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                
                let description = "";
                let body_text = news["blocks"]["body"];
                for(let j = 0; j < body_text.length; ++j)
                    description += body_text[j]["bodyHtml"];

                let single = {
                    "article-id": news["id"],
                    "section-url": news["webUrl"],
                    "title": news["webTitle"],
                    "section": news["sectionName"],
                    "date": news["webPublicationDate"],
                    "format-date" : formatDate(news["webPublicationDate"]),
                    "description": description,
                    "image": img
                };
                return_data.push(single);
            }
            if(return_data.length === 10)
                break;
        }
        // console.log(JSON.stringify(return_data))
        return JSON.stringify(return_data);
    } catch (e) {
        console.log("Error!");
        return JSON.stringify({});
    }
}

router.get('/guardian/:id', function(req, res, next) {
  const key = "587587aa-99e6-4cf9-ac7f-2baee03a3007";
  const id = req.params.id;
  const url = "https://content.guardianapis.com/"+ id + "?api-key=" + key + "&show-blocks=all";
  request(url, function (error, response, body) {
      res.send(parse_guardian_data(JSON.parse(body)));
  })
});

router.get('/guardian-search/:q', function(req, res, next) {
    const key = "587587aa-99e6-4cf9-ac7f-2baee03a3007";
    const q = req.params.q;
    const url = "https://content.guardianapis.com/search?q=" + q + "&api-key=" + key + "&show-blocks=all";
    request(url, function (error, response, body) {
        res.send(parse_guardian_data(JSON.parse(body)));
    })
});

router.get('/guardian-article', function(req, res, next) {
    console.log(req.query.id);
    const key = "587587aa-99e6-4cf9-ac7f-2baee03a3007";
    const id = req.query.id;
    const url = "https://content.guardianapis.com/" + id + "?api-key="+ key + "&show-blocks=all";
    request(url, function (error, response, body) {
        res.send(parse_guardian_data(JSON.parse(body)));
    })
});

// new api in hw9 

const googleTrends = require('google-trends-api');
router.get('/google-trends/:q', function(req, res, next) {
    const q = req.params.q;
    googleTrends.interestOverTime({keyword: q, startTime: new Date('2019-06-01')})
    .then(function(results) {
        // console.log(results['default']);
        timeline = [];
        results = JSON.parse(results);
        results = results['default']['timelineData'];
        for(let i = 0; i < results.length; ++i)
        {
            let val = results[i]['value'];
            if(val.length === 1)
                timeline.push(val[0]);
            else
                timeline.push(0);
        }
        res.send(JSON.stringify(timeline));
    })
    .catch(function(err) {
        console.log(err);
    });
});


function parse_latest_news(data) {
    try{
        console.log(data);
        let results;
        results = data["response"]["results"];   

        let return_data = [];
        for(let i = 0; i < results.length; ++i)
        {
            let news = results[i];
            if(checkValid(news["sectionName"]) && checkValid(news["webTitle"]) && checkValid(news["webUrl"]) &&
                checkValid(news["id"]) && checkValid(news["webPublicationDate"]))
            {
                let img = "";
                if(checkValid(news["fields"]["thumbnail"]))
                    img = news["fields"]["thumbnail"];
                else
                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                let single = {
                    "article-id": news["id"],
                    "section-url": news["webUrl"],
                    "title": news["webTitle"],
                    "section": news["sectionName"],
                    "date": news["webPublicationDate"],
                    "format-date" : formatDate(news["webPublicationDate"]),
                    "description": "",
                    "image": img
                };
                return_data.push(single);
            }
            if(return_data.length === 10)
                break;
        }
        // console.log(JSON.stringify(return_data))
        return JSON.stringify(return_data);
    } catch (e) {
        console.log("Error!");
        return JSON.stringify({});
    }
}


router.get('/guardian/', function(req, res, next) {
  const key = "587587aa-99e6-4cf9-ac7f-2baee03a3007";
  const url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=" + key;
  request(url, function (error, response, body) {
      res.send(parse_latest_news(JSON.parse(body)));
  })
});




module.exports = router;
