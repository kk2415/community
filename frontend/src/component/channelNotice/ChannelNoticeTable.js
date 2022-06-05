import React, {useState, useEffect, useContext, useLayoutEffect} from 'react';
import {Table, Container, Col, Row, Form, Button, Card} from 'react-bootstrap'
import ArticleTableRow from '../ArticleTableRow'
import ChannelService from '../../api/ChannelService'

export const ChannelNoticeTable = ({notices, channelId, page}) => {
    return (
        <>
            {
                notices &&
                <Table>
                    <caption className="caption-top fs-3 fw-bold">게시글</caption>
                    <thead>
                    <tr className="table-active">
                        <th scope="col">번호</th>
                        <th scope="col">글쓴이</th>
                        <th scope="col">제목</th>
                        <th scope="col">조회수</th>
                        <th scope="col">추천</th>
                        <th scope="col">작성일</th>
                    </tr>
                    </thead>
                    <tbody id="tableBody">
                    {
                        notices.map((info) => (
                            <ArticleTableRow
                                info={info}
                                url={'/channel-notice/detail/' + info.id + '?channel=' + channelId} />
                        ))
                    }
                    </tbody>
                </Table>
            }
        </>
    );
};