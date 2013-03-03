#import "Game.h"
#import "Team.h"
#import "MemoryManager.h"

@interface VC_Upcoming : UITableViewController

@property (strong, nonatomic) MemoryManager *myManager;
@property (nonatomic,strong) __block NSArray *gameArray;
@property (nonatomic,strong) __block NSArray *teamArray;
@property (nonatomic,strong) __block NSArray *yourTeamArray;

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(IBAction)helpButtonPressed:id;
-(void)initButtons;

@end

@implementation VC_Upcoming

- (id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    [self initButtons];
    
    _myManager = [[MemoryManager alloc] init];
    _gameArray = [_myManager getGameArray];
    if(_gameArray.count <= 0) {
        [_myManager refreshData];
        _gameArray = [_myManager getGameArray];
    }
    
    _teamArray = [_myManager getTeamArray];
    _yourTeamArray = [_myManager getYourTeamArray];
    _gameArray = [self trimGameArray];
    [self.tableView reloadData];
}

-(NSArray *)trimGameArray {
    NSMutableArray *gameMutable = [[NSMutableArray alloc] initWithArray:_gameArray];
    for(int i=gameMutable.count-1;i>=0;i--)
        if(![self isYourGame:[gameMutable objectAtIndex:i]])
            [gameMutable removeObjectAtIndex:i];
    return [NSArray arrayWithArray:gameMutable];
}
                 
-(BOOL)isYourGame:(Game *)gameToTest {
    for(int i=0;i<_yourTeamArray.count;i++) {
        Team *teamAtIndex = [_yourTeamArray objectAtIndex:i];
        if([gameToTest.homeTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
        if([gameToTest.awayTeam isEqualToString:teamAtIndex.teamName] && [gameToTest.league isEqualToString:teamAtIndex.league])
            return YES;
    }
    return NO;
}

-(IBAction)refreshData:(id)sender {
    _myManager = [[MemoryManager alloc]init];
    [_myManager refreshData];
    
    _gameArray = [_myManager getGameArray];
    _teamArray = [_myManager getTeamArray];
    _yourTeamArray = [_myManager getYourTeamArray];
    
    _gameArray = [self trimGameArray];
    [self.tableView reloadData];
}

-(IBAction)settingsButtonPressed:(id)sender {
    UIViewController *vc_Settings = [[self storyboard] instantiateViewControllerWithIdentifier:@"id_vc_settings"];
    vc_Settings.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:vc_Settings animated:YES completion:nil];
}

-(IBAction)helpButtonPressed:(id)sender {
    UIViewController *vc_Help = [[self storyboard] instantiateViewControllerWithIdentifier:@"id_vc_help"];
    vc_Help.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:vc_Help animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *help = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"help.png"] style:UIBarButtonItemStylePlain target:self action:@selector(helpButtonPressed:)];
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(refreshData:)];
    
    NSArray *buttons = [[NSMutableArray alloc] initWithObjects:settings,help,nil];
    self.navigationItem.rightBarButtonItems = buttons;
    self.navigationItem.leftBarButtonItem = refresh;
}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _gameArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"id_cell_upcomingGames";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) 
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    
    UILabel *homeLabel = (UILabel *)[cell viewWithTag:1];
    UILabel *awayLabel = (UILabel *)[cell viewWithTag:2];
    UILabel *dayDateTimeLabel = (UILabel *)[cell viewWithTag:3];
    UILabel *leagueLabel = (UILabel *)[cell viewWithTag:4];
    UILabel *rinkLabel = (UILabel *)[cell viewWithTag:5];

    Game *temp = ((Game *)[_gameArray objectAtIndex:indexPath.row]);
    
    homeLabel.text =  temp.homeTeam;
    awayLabel.text = temp.awayTeam;
    leagueLabel.text = temp.league;
    rinkLabel.text = temp.rink;
    dayDateTimeLabel.text = [NSString stringWithFormat:@"%@ on %@, at %@",temp.day,temp.date,temp.startTime];
    
    return cell;
}

@end