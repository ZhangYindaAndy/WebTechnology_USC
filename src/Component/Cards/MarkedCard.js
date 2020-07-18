import React, {Component} from "react";
import {Card, Col, Container, Image, Row} from "react-bootstrap";
import { withRouter } from 'react-router-dom'
import {MdDelete} from "react-icons/md";

import renderTag from "./utils";
import ShareButton from "../ShareButton";
import TextTruncate from "react-text-truncate";
import "../Components.css";
import {toast, Zoom} from "react-toastify";


class MarkedCard extends Component {
    constructor(props) {
        super(props);
    }

    handleClick() {
        this.props.history.push("/article?id=" + this.props.cardData["article-id"] + "&source=" + this.props.cardData["source"]);
    }

    handleDelete(event) {
        event.stopPropagation();

        toast("Removing - " + this.props.cardData["title"],{
            className: "toast-massage",
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: false,
            draggable: true,
            transition: Zoom
        });

        this.props.deleteMarkedInfo(this.props.cardData["article-id"]);
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
                                            className="inline-display"
                                        />
                                    </span>
                                    <span className="inline-display">
                                        <MdDelete
                                            onClick={this.handleDelete.bind(this)}
                                            className="inline-display"
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
                            <Col lg={4} sm={4} xs={4} className="font-italic pr-0">
                                {this.props.cardData["date"]}
                            </Col>
                            <Col lg={8} sm={8} xs={8} className="text-right pl-0">
                                {renderTag(this.props.cardData["section"])}
                                {renderTag(this.props.cardData["source"])}
                            </Col>
                        </Row>
                    </Card.Body>
                </Card>
            </Container>
        )
    }
}

export default withRouter(MarkedCard);
