import React, {Component} from "react";
import {Card, Col, Container, Image, Row} from "react-bootstrap";
import { withRouter } from 'react-router-dom'

import renderTag from "./utils";
import ShareButton from "../ShareButton";
import TextTruncate from "react-text-truncate";

import "../Components.css";


class SearchCard extends Component {
    constructor(props) {
        super(props);
    }

    handleClick() {
        this.props.history.push("/article?id=" + this.props.cardData["article-id"] + "&source=" + this.props.cardData["source"]);
    }

    render() {
        return (
            <Container>
                <Card
                    className="mt-2 md-2"
                    onClick={this.handleClick.bind(this)}
                    style={{cursor: "pointer"}}
                >
                    <Card.Body>
                        <Row>
                            <Col>
                                <Card.Title className="inline-display">
                                    <span className="small-card-title">
                                        <TextTruncate
                                            className="font-italic"
                                            line={2}
                                            element="span"
                                            truncateText="..."
                                            text={this.props.cardData["title"]}
                                        />
                                    </span>
                                    <span className="inline-display">
                                        <ShareButton
                                            title={this.props.cardData["title"]}
                                            url={this.props.cardData["section-url"]}
                                            source={this.props.cardData["source"]}
                                        />
                                    </span>
                                </Card.Title>
                            </Col>
                        </Row>

                        <Row>
                            <Col>
                                <Image
                                    className="rounded"
                                    src={this.props.cardData["image"]}
                                    thumbnail
                                />
                            </Col>

                        </Row>

                        <Row>
                            <Col className="font-italic pr-0">{this.props.cardData["date"]}</Col>
                            <Col className="text-right pl-0">{renderTag(this.props.cardData["section"])}</Col>
                        </Row>
                    </Card.Body>
                </Card>
            </Container>
        )
    }
}

export default withRouter(SearchCard);
