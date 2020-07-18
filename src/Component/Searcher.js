import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import debounce from "lodash/debounce"
import AsyncSelect from 'react-select/async';


class Searcher extends Component {
    constructor(props) {
        super(props);
    }

    searchNews = (inputOption) => {
        this.setState({q: inputOption.value});
        this.props.history.push("/search/" + inputOption.value);
    };


    autoSuggest = async (inputValue) => {
        let suggestValue = [{value: inputValue, label: inputValue}];
        try {
            const response = await fetch(
                "https://andyzhang.cognitiveservices.azure.com/bing/v7.0/suggestions?mkt=en-US&q=" + inputValue,
                {
                    headers: {
                        "Ocp-Apim-Subscription-Key": "73eb70a0f83746baaf06d92fc5a941bc"
                    }
                }
            );
            const jsonValue = await response.json();
            let groups = jsonValue["suggestionGroups"][0]["searchSuggestions"];
            // console.log(groups);
            for(let i = 0; i < groups.length; i++)
            {
                suggestValue.push({value: groups[i]["displayText"], label: groups[i]["displayText"]});
            }
            // console.log(suggestValue);
            return suggestValue;
        } catch (error) {
            console.error(`Error fetching search`);
            return suggestValue;
        }
    };


    render() {
        if(this.props.isOnSearch)
            return (
                <AsyncSelect
                    placeholder={"Enter keyword .."}
                    cacheOptions
                    loadOptions={debounce(this.autoSuggest, 1000, {leading: true})}
                    onChange={this.searchNews.bind(this)}
                />
            )
        else
            return (
                <AsyncSelect
                    placeholder={"Enter keyword .."}
                    value={null}
                    cacheOptions
                    loadOptions={debounce(this.autoSuggest, 1000, {leading: true})}
                    onChange={this.searchNews.bind(this)}
                />
            )
    }
}

export default withRouter(Searcher);