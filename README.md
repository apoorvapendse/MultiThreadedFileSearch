# Multi Threaded File Searcher

## CLI Arguments

### Indexing
- `-i`: Builds index for the files in the current working directory and saves it in `mtfs-index.txt`.
- `-r "<PATH>"`: Builds index for the directory passed as `<PATH>`.

### Searching
- `-s "<SEARCH_TERM>"`: Performs a multi-threaded search for `<SEARCH_TERM>` in the indexed files. The results are displayed on the console.
- `-f "<SEARCH_TERM>"`: Performs a single-threaded search for `<SEARCH_TERM>` in the indexed files. The results are displayed on the console.

### Others
- `-igf "<FILES>"`: Ignore specific files during indexing, where `<FILES>` is a comma-separated list of file names.
- `-igd "<DIRECTORIES>"`: Ignore specific directories during indexing, where `<DIRECTORIES>` is a comma-separated list of directory names.
- `-ige "<EXTENSIONS>"`: Ignore specific file extensions during indexing, where `<EXTENSIONS>` is a comma-separated list of file extensions.

**NOTE:**
1. Multithreaded search is performed using the maximum available physical threads on the CPU.
2. Searching is case-insensitive.
3. Common folders such as `.git` and `node_modules` and files with extensions `.class` and `.gz` are ignored by default.

## Features Implemented
- `CLIManager` package for parsing out CLI args.
- Search space optimization similar to `ripgrep`.
- Multi-threaded file content searching based on a work-stealing queue.
- Single-threaded file content search.
- Fuzzy searching algorithm using Levenshtein distance.
- `BoundedPriorityQueue` made thread-safe.
- `BoundedPriorityQueue` to store search results.
- `Serializer` class to cache indexed file tree in a file.
- Custom `ThreadPoolManager`.
- `ThreadSafeQueue`.
- Multi-threaded file indexing.
- Multi-threaded BFS file searching.
- Single-threaded DFS file searching.
- Single-threaded BFS file searching.
- Single-threaded file indexing.
