var express = require('express');
var request = require('request');
var router = express.Router();

router.get('/', (req, res) => {
    res.send("Yinda Zhang Server");
})

function formatDate(date_str)
{
    let date = new Date(date_str);
    let month = '' + (date.getMonth() + 1);
    let day = '' + date.getDate();
    let year = date.getFullYear();
    if(month.length < 2)
        month = '0' + month;
    if(day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
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
            if(checkValid(news["sectionId"]) && checkValid(news["webTitle"]) && checkValid(news["webUrl"]) &&
                checkValid(news["id"]) && checkValid(news["webPublicationDate"]) &&
                checkValid(news["blocks"]["body"][0]["bodyTextSummary"]))
            {
                let img = "";
                if(news["blocks"]["main"] === undefined)
                    continue;
                let imgs = news["blocks"]["main"]["elements"][0]["assets"];

                if(imgs.length > 0 && imgs[imgs.length-1]["file"].length > 0)
                    img = imgs[imgs.length - 1]["file"];
                else
                    img = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                let single = {
                    "article-id": news["id"],
                    "section-url": news["webUrl"],
                    "title": news["webTitle"],
                    "section": news["sectionId"],
                    "date": formatDate(news["webPublicationDate"]),
                    "description": news["blocks"]["body"][0]["bodyTextSummary"],
                    "image": img,
                    "source": "guardian"
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
  const url = "https://content.guardianapis.com/search?api-key=" + key + "&section=(sport|business|technology|politics)&show-blocks=all";
  request(url, function (error, response, body) {
      res.send(parse_guardian_data(JSON.parse(body)));
  })
});


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

//NYTimes Search
// search only generate small card, artical base, but the url also called section-url
function parse_nytimes_search(data)
{
    try{
        let results = data["response"]["docs"];
        let return_data = [];
        // console.log(results);
        for(let i = 0; i < results.length; ++i)
        {
            let news = results[i];
            if(checkValid(news["headline"]["main"]) && checkValid(news["news_desk"]) &&
                checkValid(news["pub_date"]) && checkValid(news["web_url"]) && checkValid(news["abstract"]))
            {
                let img = "";
                let imgs = news["multimedia"];
                for(let j = 0; j < imgs.length; ++j)
                {
                    if(parseInt(imgs[j]["width"]) >= 2000)
                    {
                        img = "https://www.nytimes.com/" + imgs[j]["url"];
                        break;
                    }
                }
                if(img.length === 0)
                    img = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
                let single = {
                    "article-id": news["web_url"],
                    "section-url": news["web_url"],
                    "title": news["headline"]["main"],
                    "section": news["news_desk"],
                    "date": formatDate(news["pub_date"]),
                    "description": news["abstract"],
                    "image": img,
                    "source": "nytimes"
                }
                return_data.push(single);
            }
            if(return_data.length === 10)
                break;
        }
        // console.log(return_data)
        return JSON.stringify(return_data);
    } catch (e) {
        console.log("Error");
        return JSON.stringify({})
    }
}

function parse_nytimes_data(data)
{
    try {
        // console.log(data);
        let results = data["results"];
        // console.log(data["results"].length);
        let return_data = [];
        for(let i = 0; i < results.length; ++i)
        {
            let news = results[i];
            if(checkValid(news["section"]) && checkValid(news["title"]) && checkValid(news["url"]) &&
                checkValid(news["published_date"]) && checkValid(news["abstract"]))
            {
                let img = "";
                let imgs = news["multimedia"];
                // console.log(imgs)
                for(let j = 0; j < imgs.length; ++j)
                {
                    if(parseInt(imgs[j]["width"]) >= 2000)
                    {
                        img = imgs[j]["url"];
                        break;
                    }
                }
                if(img.length === 0)
                    img = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Nytimes_hq.jpg";
                let single = {
                    "article-id": news["url"],
                    "section-url": news["url"],
                    "title": news["title"],
                    "section": news["section"],
                    "date": formatDate(news["published_date"]),
                    "description": news["abstract"],
                    "image": img,
                    "source": "nytimes"
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

router.get('/nytimes/', function(req, res, next) {
  // const key = "XGlTZJg9IZVq5lMgUvxcmXMSMGOSV66n";
  const key = "4XOyLx6NZH0jMW5NWfQbENWiadGAVRr8";
  const url = "https://api.nytimes.com/svc/topstories/v2/home.json?api-key=" + key;
  request(url, function (error, response, body) {
      res.send(parse_nytimes_data(JSON.parse(body)));
  })
});

router.get('/nytimes/:id', function(req, res, next) {
  // const key = "XGlTZJg9IZVq5lMgUvxcmXMSMGOSV66n";
  const key = "4XOyLx6NZH0jMW5NWfQbENWiadGAVRr8";
  let id = req.params.id;
  if(id === "sport")
    id += "s";
  const url = "https://api.nytimes.com/svc/topstories/v2/" + id + ".json?api-key=" + key;
  request(url, function (error, response, body) {
      res.send(parse_nytimes_data(JSON.parse(body)));
  })
});

router.get('/nytimes-search/:q', function(req, res, next) {
    // const key = "XGlTZJg9IZVq5lMgUvxcmXMSMGOSV66n";
    const key = "4XOyLx6NZH0jMW5NWfQbENWiadGAVRr8";
    const q = req.params.q;
    const url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + q + "&api-key=" + key;
    request(url, function (error, response, body) {
        res.send(parse_nytimes_search(JSON.parse(body)));
    })
});

router.get('/nytimes-article', function(req, res, next) {
    const key = "4XOyLx6NZH0jMW5NWfQbENWiadGAVRr8";
    const id = req.query.id;
    let url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=web_url:(\"" + id + "\")&api-key=" + key;
    request(url, function (error, response, body) {
        res.send(parse_nytimes_search(JSON.parse(body)));
    })
});


module.exports = router;
