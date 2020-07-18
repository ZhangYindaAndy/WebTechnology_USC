import React, { Component } from "react";
import {Card, Row, Col, Container, Image} from "react-bootstrap";
import ShareButton from "../ShareButton";
import TextTruncate from "react-text-truncate";
import { withRouter } from 'react-router-dom';

import renderTag from "./utils";
import "../Components.css";

class SectionCard extends Component {
    constructor(props) {
        super(props);
    }

    handleClick() {
        this.props.history.push("/article?id=" + this.props.id + "&source=" + this.props.source);
    }

    render() {
        return (
            <Container fluid>
                <Card
                    onClick={this.handleClick.bind(this)}
                    className="border rounded bg-white mt-4 shadow p-0"
                    style={{cursor: "pointer"}}
                >
                    <Row>
                        <Col lg={3} sm={11} xs={11}>
                            <Image
                                className="rounded m-3"
                                src={this.props.image}
                                thumbnail
                            />
                        </Col>
                        <Col>
                            <Card.Body className="mr-3">
                                <Row>
                                    <Col>
                                        <Card.Title className="inline-display">
                                            <span className="section-card-title">
                                                {this.props.title}
                                                <ShareButton
                                                    title={this.props.title}
                                                    url={this.props.url}
                                                    source={this.props.source}
                                                />
                                            </span>
                                        </Card.Title>
                                    </Col>
                                </Row>

                                <Row>
                                    <Col>
                                        <TextTruncate
                                            line={3}
                                            element="span"
                                            truncateText="..."
                                            text={this.props.description}
                                        />
                                    </Col>
                                </Row>

                                <br/>
                                <br/>

                                <Row>
                                    <Col className="font-italic">{this.props.date}</Col>
                                    <Col className="text-right">{renderTag(this.props.section)}</Col>
                                </Row>

                            </Card.Body>
                        </Col>
                    </Row>
                </Card>

            </Container>

            // <Container
            //     className="border rounded bg-white m-4 shadow"
            //     onClick={this.handleClick.bind(this)}
            //     fluid
            // >
            //         <Row lg={2}>
            //             <Col lg={2} sm={12}>
            //                 <Image
            //                     className="rounded float-left"
            //                     src={this.props.image}
            //                     thumbnail
            //                 />
            //             </Col>
            //
            //             <Col>
            //                 <Row className="m-2">
            //                     <Col>
            //                         <div className="section-card-title">
            //                             {this.props.title}
            //                             <ShareButton
            //                                 title={this.props.title}
            //                                 url={this.props.url}
            //                                 source={this.props.source}
            //                             />
            //                         </div>
            //                     </Col>
            //                 </Row>
            //                 <Row>
            //                     <Col>
            //                         <TextTruncate
            //                             line={3}
            //                             element="span"
            //                             truncateText="..."
            //                             text={this.props.description}
            //                         />
            //                     </Col>
            //                 </Row>
            //                 <br/>
            //                 <Row>
            //                     <Col className="font-italic">
            //                         {this.props.date}
            //                     </Col>
            //                     <Col className="text-right">
            //                         {this.props.section}
            //                     </Col>
            //                 </Row>
            //             </Col>
            //         </Row>
            // </Container>
        )
    }
}

export default withRouter(SectionCard);