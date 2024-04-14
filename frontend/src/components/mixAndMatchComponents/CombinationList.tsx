import React from "react";
import SearchGameList from "./SearchGameList";
import RelatedTagList from "./RelatedTagList";

const CombinationList: React.FC = () => {
  return (
    <div>
      <SearchGameList />
      <RelatedTagList />
    </div>
  );
};

export default CombinationList;
