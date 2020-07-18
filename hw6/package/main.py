
from newsapi import NewsApiClient
from flask import Flask, request, jsonify

news_api = NewsApiClient(api_key='d69fa18bf2fc45e688a2007f015cdcdc')
app = Flask(__name__)
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 10


def is_valid(item):
    if item is not None and len(item) > 0:
        return True
    return False

def get_headlines_data(sources, return_num):
    headlines = news_api.get_top_headlines(sources=sources, page_size=30)
    headlines = headlines['articles']
    num = 0
    valid_news = []
    for article in headlines:
        if is_valid(article['author']) and is_valid(article['title']) and is_valid(article['description']) and \
           is_valid(article['url']) and is_valid(article['urlToImage']) and is_valid(article['publishedAt']) and \
           is_valid(article['content']) and isinstance(article['source'], dict) and 'id' in article['source'] and \
           'name' in article['source'] and is_valid(article['source']['id']) and is_valid(article['source']['name']):
            valid_news.append(article)
            num += 1
            if num == return_num:
                break
    return {'articles': valid_news}


@app.route('/')
def root():
    return app.send_static_file('index.html')

@app.route('/index/<source>', methods=['GET'])
def init_data(source):
    if source == 'slides':
        return jsonify(get_headlines_data('', 5))
    elif source == 'cnn':
        return jsonify(get_headlines_data('cnn', 4))
    elif source == 'fox':
        return jsonify(get_headlines_data('fox-news', 4))


@app.route('/sources/<name>', methods=['GET'])
def load_sources(name):
    if name == 'all':
        sources = news_api.get_sources(language='en', country='us')
    else:
        sources = news_api.get_sources(category=name, language='en', country='us')
    sources = sources['sources']

    source_dict = {}
    for source in sources:
        if is_valid(source['id']) and is_valid(source['name']) and source['name'] not in source_dict:
            source_dict[source['name']] = source['id']
            if len(source_dict) == 10:
                break;
    source_list = []
    for k, v in source_dict.items():
        source_list.append({'id': v, 'name': k})
    return jsonify({'sources': source_list})


@app.route('/cloud', methods=['GET'])
def load_cloud():
    with open('./static/stopwords_en.txt', 'r') as f:
        words = f.readlines()
    stopwords = set()
    for word in words:
        stopwords.add(word.strip())

    headlines = news_api.get_top_headlines(country='us', page_size=30)
    headlines = headlines['articles']
    wordcount = {}
    for news in headlines:
        if is_valid(news['title']):
            title = news['title'].split()
            for word in title:
                word = word.strip('\'\",:?!.-&|').lower()
                if len(word) > 0 and word not in stopwords:
                    if word not in wordcount:
                        wordcount[word] = 1
                    wordcount[word] += 1

    wordcount = sorted(wordcount.items(), key=lambda x: -x[1])
    if len(wordcount) > 30:
        wordcount = wordcount[:30]
    count = [v for k, v in wordcount]
    Max = max(count)
    Min = min(count)

    myword = [{'word': k, 'size': str(int((v+2-Min*1.0)/(Max-Min)*25))} for k, v in wordcount]
    return jsonify({'words': myword})


@app.route('/query/<queries>', methods=['GET'])
def query_data(queries):
    print(queries)
    queries = queries.split('&')
    keyword = queries[0]
    date_from = queries[1]
    date_to = queries[2]
    source = queries[3]

    try:
        if source == 'all':
            news = news_api.get_everything(q=keyword, from_param=date_from, to=date_to, page_size=60)
        else:
            news = news_api.get_everything(q=keyword, sources=source, from_param=date_from, to=date_to, page_size=60)
    except Exception as e:
        return jsonify({'error': eval(str(e))})

    news = news['articles']

    num = 0
    valid_news = []
    for article in news:
        if is_valid(article['author']) and is_valid(article['title']) and is_valid(article['description']) and \
           is_valid(article['url']) and is_valid(article['urlToImage']) and is_valid(article['publishedAt']) and \
           is_valid(article['content']) and isinstance(article['source'], dict) and 'id' in article['source'] and \
           'name' in article['source'] and is_valid(article['source']['id']) and is_valid(article['source']['name']):
            valid_news.append(article)
            num += 1
            if num == 15:
                break
    return jsonify({'articles': valid_news}) 


if __name__ == '__main__':
    app.run(debug=True)






