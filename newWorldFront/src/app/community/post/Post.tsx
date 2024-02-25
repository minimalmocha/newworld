"use client";
import React from "react";
import styles from "./post.module.scss";
import Tiptap from "@/components/tiptap/Tiptap";

const Post = (session: any) => {
  console.log(session.session.user.nickname);
  const userNickName = session.session.user.nickname;
  return (
    <div className={styles.background}>
      <div className={styles.container}>
        <form className={styles.contents_section}>
          <Tiptap content="Hello, world!" nickname={userNickName} />
        </form>
      </div>
    </div>
  );
};

export default Post;
