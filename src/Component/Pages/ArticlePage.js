import React, {Component} from "react";

import Spinner from "../Spinner";
import ArticleCard from "../Cards/ArticleCard";


export default class ArticlePage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isRead: false, pageData: []
        }
    }

    componentDidMount() {
        let param = new URLSearchParams(this.props.location.search);
        let id = param.get("id");
        let source = param.get("source");

        if(source === "guardian")
            fetch("https://yindahw8.appspot.com/guardian-article?id=" + id)
                .then(res => res.json())
                .then(
                    (result) => {
                        this.setState({
                            isRead: true,
                            pageData: result,
                        })
                    }
                )
        else
            fetch("https://yindahw8.appspot.com/nytimes-article?id=" + id)
                .then(res => res.json())
                .then(
                    (result) => {
                        // console.log(result.length);
                        this.setState({
                            isRead: true,
                            pageData: result
                        })
                    }
                )
    }
    // can only appear one
    render () {
        if(!this.state.isRead) {
            return (
                <Spinner />
            )
        }
        else {
            return (
                <ArticleCard pageData={this.state.pageData[0]} />
            )
        }
    }
}




