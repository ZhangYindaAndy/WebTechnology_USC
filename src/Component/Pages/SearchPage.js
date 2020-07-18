import React, {Component} from "react";

import Spinner from "../Spinner";
import SearchCard from "../Cards/SearchCard";
import {Col, Container, Row} from "react-bootstrap";


export default class SearchPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isRead: false, guardianData: [], nytimeData: [], q: ""
        };
        this.createSearchCard = this.createSearchCard.bind(this);
    }

    createSearchCard(cards) {
        let card_list = [];
        for(let i = 0; i < cards.length; ++i)
        {
            if(i === 5)
                break;
            card_list.push(
                <Col lg={3} sm={12} xs={12} className="m-0 p-0">
                    <SearchCard cardData={cards[i]} />
                </Col>
            );
        }
        return card_list;
    }

    componentDidMount() {

        fetch("https://yindahw8.appspot.com/guardian-search/" + this.props.match.params.q)
            .then(res => res.json())
            .then(
                (result) => {
                    // console.log(result.length);
                    this.setState({
                        isRead: true,
                        guardianData: result,
                        q: this.props.match.params.q
                    })
                }
            )

        fetch("https://yindahw8.appspot.com/nytimes-search/" + this.props.match.params.q)
            .then(res => res.json())
            .then(
                (result) => {
                    // console.log(result.length);
                    this.setState({
                        isRead: true,
                        nytimeData: result,
                        q: this.props.match.params.q
                    })
                }
            )
    }
    // re-search
    componentDidUpdate(prevProps, prevState, snapshot) {
        // console.log(this.props.match.params.q);
        // console.log(prevProps.match.params.q);
        // console.log(this.state.q);
        if(this.props.match.params.q !== prevProps.match.params.q) {
            this.setState({
                isRead: false,
                q: this.props.match.params.q
            })
            this.componentDidMount();
        }
    }

    // can only appear one
    render() {
        // console.log(this.props.match.params.q);
        if(!this.state.isRead) {
            return (
                <Spinner />
            )
        }
        else {
            return (
                <Container fluid className="mt-2">
                    <Row>
                        <Col>
                            <h4>Results</h4>
                        </Col>
                    </Row>
                    <Row lg={4} sm={1} xs={1}>
                        {this.createSearchCard(this.state.guardianData)}
                        {this.createSearchCard(this.state.nytimeData)}
                    </Row>
                </Container>
            )
        }

    }
}




