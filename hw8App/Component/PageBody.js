import React, { Component } from 'react';

import SectionCard from "./Cards/SectionCard";
import Spinner from "./Spinner";

import {Container, Row} from "react-bootstrap";


class PageBody extends Component{
    constructor(props) {
        super(props);
        this.state = { guardData: [], nytimeData: [], isRead: false }
    }


    componentDidMount() {
        fetch("https://yindahw8.appspot.com/guardian" + this.props.section)
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isRead: true,
                        guardData: result,
                    })
                }
            )
        fetch("https://yindahw8.appspot.com/nytimes" + this.props.section)
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result.length);
                    this.setState({
                        isRead: true,
                        nytimeData: result
                    })
                }
            )
    }

    createSectionCard(cards) {
        let card_list = [];
        for(let i = 0; i < cards.length; ++i)
        {
            card_list.push(<SectionCard
                key={i}
                title={cards[i]["title"]}
                image={cards[i]["image"]}
                section={cards[i]["section"]}
                description={cards[i]["description"]}
                date={cards[i]["date"]}
                url={cards[i]["section-url"]}
                id={cards[i]["article-id"]}
                source={cards[i]["source"]}
            />);
        }
        return card_list;
    }


    render() {
        if(!this.state.isRead)
        {
            return (
                <Spinner />
            )
        }
        else if(this.props.apiBase) // guardian
        {
            return (
                <div>
                    {this.createSectionCard(this.state.guardData)}
                </div>
            )
        }
        else  // nytimes
        {
            return (
                <div>
                    {this.createSectionCard(this.state.nytimeData)}
                </div>
            )
        }
    }

}

export default PageBody;