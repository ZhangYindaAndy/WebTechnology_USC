import React, { Component } from 'react';
import {Col, Container, Row} from "react-bootstrap";
import MarkedCard from "../Cards/MarkedCard";


class BookmarkPage extends Component {
    constructor(props) {
        super(props);
        let savedInfo = localStorage.getItem("savedInfo");
        if(savedInfo == null)
            savedInfo = {};   // use article-id as the key of each record
        else
            savedInfo = JSON.parse(savedInfo);
        // console.log(savedInfo);
        this.state = { markedInfo: savedInfo };
    }

    renderMarkedCards() {
        let markedCards = [];
        for(let id in this.state.markedInfo)
        {
            markedCards.push(
                <Col lg={3} sm={12} xs={12} className="m-0 p-0">
                    <MarkedCard
                        cardData={this.state.markedInfo[id]}
                        deleteMarkedInfo={this.deleteMarkedInfo.bind(this)}
                    />
                </Col>
            );
        }
        return markedCards;
    }

    deleteMarkedInfo(id) {
        delete this.state.markedInfo[id];
        this.setState({
            markedInfo: this.state.markedInfo
        });
        localStorage.setItem("savedInfo", JSON.stringify(this.state.markedInfo)); // Might be bug
    }

    render() {
        if(JSON.stringify(this.state.markedInfo) === "{}")
        {
            return (
                <h4 className="text-center">You have no saved articles</h4>
            )
        }
        else
        {
            return (
                <Container fluid className="mt-2">
                    <Row>
                        <Col>
                            <h4>Favorites</h4>
                        </Col>
                    </Row>
                    <Row lg={4} sm={1} xs={1}>
                        {this.renderMarkedCards()}
                    </Row>
                </Container>

            )
        }

    }
}

export default BookmarkPage;